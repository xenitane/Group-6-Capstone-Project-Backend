package mainpackage.socMedApp.service;


import mainpackage.socMedApp.model.DeletePostRequest;
import mainpackage.socMedApp.model.ReactResponse;
import mainpackage.socMedApp.model.post.*;
import mainpackage.socMedApp.model.user.ProfileHead;
import mainpackage.socMedApp.model.user.User;
import mainpackage.socMedApp.model.user.UserRole;
import mainpackage.socMedApp.repository.PostRepository;
import mainpackage.socMedApp.repository.UserRepository;
import mainpackage.socMedApp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PostService {

	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;

	public PostResponse savePost(Post post) {
		PostResponse postResponse = new PostResponse();
		if (post == null || post.getTimestamp() == null || post.getContentType() == null) {
			postResponse.setStatus(false);
			postResponse.setMessage("invalid post");
			return postResponse;
		}
		if (!userRepository.existsById(post.getAuthorId())) {
			postResponse.setStatus(false);
			postResponse.setMessage("Sign up to create a post.");
			return postResponse;
		}
		boolean text = post.getContentType() == PostContentType.TEXT;
		boolean image = post.getContentType() == PostContentType.IMAGE;
		boolean emptyText = post.getText() == null || post.getText().length() == 0;
		boolean emptyImage = post.getImageURL() == null || post.getImageURL().length() == 0;
		boolean emptyCaption = post.getImageCaption() == null || post.getImageCaption().length() == 0;
		if (!(text && emptyCaption && emptyImage && !emptyText) && !(image && emptyText && !emptyImage)) {
			postResponse.setStatus(false);
			postResponse.setMessage("Invalid post parameters");
		} else {
			String postId;
			do postId = Generator.idGen(); while (postRepository.existsById(postId));
			post.setId(postId);
			post.setLikeCount(0);
			post.setCommentCount(0);
			post.setUserIdsWhoLikedThisPost(new HashSet<>());
			post.setCommentIDsOnThisPost(new HashSet<>());
			postResponse.setStatus(true);
			postResponse.setMessage("Saving post");
			postResponse.setPostId(postId);
			postRepository.save(post);
		}
		return postResponse;
	}


	public GetPostByIdResponse getPostById(String postId, String currentUserId) {
		GetPostByIdResponse getPostByIdResponse = new GetPostByIdResponse();
		Post post = postRepository.findById(postId).orElse(null);
		PostBody postBody = post == null ? null : new PostBody(post, currentUserId, new ProfileHead(userRepository.findById(post.getAuthorId()).orElse(null)));
		if (postBody == null) {
			getPostByIdResponse.setStatus(false);
			getPostByIdResponse.setMessage("Not post with this id");
		} else {
			getPostByIdResponse.setStatus(true);
			getPostByIdResponse.setMessage("post found");
			getPostByIdResponse.setPostBody(postBody);
		}
		return getPostByIdResponse;
	}

	public DeletePostResponse deletePost(String postId, DeletePostRequest deletePostRequest) {
		DeletePostResponse deletePostResponse = new DeletePostResponse();
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("Post not found");
			return deletePostResponse;
		}
		User user = userRepository.findById(deletePostRequest.getCurrentUserId()).orElse(null);
		boolean deletePermission = user != null && (post.getAuthorId().equals(user.getId()) || user.getRole() == UserRole.ADMIN);
		if (deletePermission) {
			postRepository.delete(post);
			deletePostResponse.setStatus(true);
			deletePostResponse.setMessage("Post deleted successfully");
		} else {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("You don't have permission to delete this post");
		}
		return deletePostResponse;
	}

	//
	public EditPostResponse editPost(String postId, EditPostRequest editPostRequest) {
		EditPostResponse editPostResponse = new EditPostResponse();
		editPostResponse.setStatus(false);
		Post existingPost = postRepository.findById(postId).orElse(null);
		if (existingPost == null) {
			editPostResponse.setMessage("post not found");
			return editPostResponse;
		}
		boolean text = existingPost.getContentType() == PostContentType.TEXT;
		User user = userRepository.findById(editPostRequest.getCurrentUserId()).orElse(null);
		boolean editPermission = user != null && existingPost.getAuthorId().equals(editPostRequest.getCurrentUserId());
		if (!editPermission) {
			editPostResponse.setMessage("You don't have permission to edit this post.");
		} else if (text && (editPostRequest.getPostData() == null || editPostRequest.getPostData().length() == 0)) {
			editPostResponse.setMessage("Invalid data sent.");
		} else {
			if(text)existingPost.setText(editPostRequest.getPostData());
			else existingPost.setImageCaption(editPostRequest.getPostData());
			postRepository.save(existingPost);
			editPostResponse.setStatus(true);
			editPostResponse.setMessage("Post updated.");
		}
		return editPostResponse;
	}

	public List<ProfileHead> getPostReactors(String postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			return null;
		} else {
			List<ProfileHead> profileHeadList = new ArrayList<>();
			for (String userId : post.getUserIdsWhoLikedThisPost())
				profileHeadList.add(new ProfileHead(userRepository.findById(userId).orElse(null)));
			return profileHeadList;
		}
	}

	public List<PostBody> getPostsByUserId(String userId, String currentUserId) {
		if (!userRepository.existsById(userId)) return new ArrayList<>();
		List<Post> postsByUser = postRepository.findAllByAuthorId(userId);
		List<PostBody> postBodiesByUserList = new ArrayList<>();
		for (Post post : postsByUser)
			postBodiesByUserList.add(new PostBody(post, currentUserId, new ProfileHead(userRepository.findById(post.getAuthorId()).orElse(null))));
		return postBodiesByUserList;
	}

	public ReactResponse doReaction(String postId, String currentUserId) {
		ReactResponse reactResponse = new ReactResponse();
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(currentUserId).orElse(null);
		if (post == null || user == null) {
			reactResponse.setStatus(false);
			reactResponse.setMessage("Either the post does not exist or we don't know who you are.");
		} else {
			reactResponse.setStatus(true);
			if (post.addReactor(currentUserId)) reactResponse.setMessage("post liked.");
			else {
				post.deleteReactor(currentUserId);
				reactResponse.setMessage("post unliked.");
			}
			postRepository.save(post);
		}
		return reactResponse;
	}
}
