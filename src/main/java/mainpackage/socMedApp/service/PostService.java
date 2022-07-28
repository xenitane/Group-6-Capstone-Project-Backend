package mainpackage.socMedApp.service;


import mainpackage.socMedApp.model.post.DeletePostRequest;
import mainpackage.socMedApp.model.post.ReactResponse;
import mainpackage.socMedApp.model.post.*;
import mainpackage.socMedApp.model.user.ProfileHead;
import mainpackage.socMedApp.model.user.User;
import mainpackage.socMedApp.model.user.UserRole;
import mainpackage.socMedApp.repository.CommentRepository;
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
	@Autowired
	CommentRepository commentRepository;

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
			postRepository.save(post);
			postResponse.setStatus(true);
			postResponse.setMessage("Saving post");
			postResponse.setPostId(postId);
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
			commentRepository.deleteAllById(post.getCommentIDsOnThisPost());
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
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			editPostResponse.setMessage("post not found");
			return editPostResponse;
		}
		boolean text = post.getContentType() == PostContentType.TEXT;
		User user = userRepository.findById(editPostRequest.getCurrentUserId()).orElse(null);
		boolean editPermission = user != null && post.getAuthorId().equals(user.getId());
		if (!editPermission) {
			editPostResponse.setMessage("You don't have permission to edit this post.");
		} else if (text && (editPostRequest.getPostData() == null || editPostRequest.getPostData().length() == 0)) {
			editPostResponse.setMessage("Invalid data sent.");
		} else {
			if (text) post.setText(editPostRequest.getPostData());
			else post.setImageCaption(editPostRequest.getPostData());
			postRepository.save(post);
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

	public List<PostBody> getPostsByUsername(String username, String currentUserId) {
		User user=userRepository.findByUsername(username).orElse(null);
		if(user==null)return new ArrayList<>();
		List<Post> postsByUser = postRepository.findAllByAuthorId(user.getId());
		List<PostBody> postBodiesByUserList = new ArrayList<>();
		for (Post post : postsByUser)
			postBodiesByUserList.add(new PostBody(post, currentUserId, new ProfileHead(user)));
		return postBodiesByUserList;
	}

	public ReactResponse doReaction(String postId, ReactRequest reactRequest) {
		ReactResponse reactResponse = new ReactResponse();
		Post post = postRepository.findById(postId).orElse(null);
		User user = userRepository.findById(reactRequest.getCurrentUserId()).orElse(null);
		if (post == null || user == null) {
			reactResponse.setStatus(false);
			reactResponse.setMessage("Either the post does not exist or we don't know who you are.");
		} else {
			reactResponse.setStatus(true);
			if (post.addReactor(reactRequest.getCurrentUserId())) reactResponse.setMessage("post liked.");
			else {
				post.deleteReactor(reactRequest.getCurrentUserId());
				reactResponse.setMessage("post unliked.");
			}
			postRepository.save(post);
		}
		return reactResponse;
	}
}
