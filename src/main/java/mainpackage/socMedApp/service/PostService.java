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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
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

	public Pair<PostResponse, HttpStatus> savePost(Post post) {
		PostResponse postResponse = new PostResponse();
		if (post == null || post.getTimestamp() == null || post.getContentType() == null) {
			postResponse.setStatus(false);
			postResponse.setMessage("invalid post.");
			return Pair.of(postResponse, HttpStatus.BAD_REQUEST);
		}
		if (!userRepository.existsById(post.getAuthorId())) {
			postResponse.setStatus(false);
			postResponse.setMessage("Sign up to create a post.");
			return Pair.of(postResponse, HttpStatus.UNAUTHORIZED);
		}
		boolean text = post.getContentType() == PostContentType.TEXT;
		boolean image = post.getContentType() == PostContentType.IMAGE;
		boolean emptyText = post.getText() == null || post.getText().length() == 0;
		boolean emptyImage = post.getImageURL() == null || post.getImageURL().length() == 0;
		boolean emptyCaption = post.getImageCaption() == null || post.getImageCaption().length() == 0;
		if (!(text && emptyCaption && emptyImage && !emptyText) && !(image && emptyText && !emptyImage)) {
			postResponse.setStatus(false);
			postResponse.setMessage("Invalid post parameters");
			return Pair.of(postResponse, HttpStatus.BAD_REQUEST);
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
			return Pair.of(postResponse, HttpStatus.CREATED);
		}
	}


	public Pair<GetPostByIdResponse, HttpStatus> getPostById(String postId, String currentUserId) {
		GetPostByIdResponse getPostByIdResponse = new GetPostByIdResponse();
		if (postId == null || postId.trim().isEmpty()) {
			getPostByIdResponse.setStatus(false);
			getPostByIdResponse.setMessage("Looking for a dead post?");
			return Pair.of(getPostByIdResponse, HttpStatus.BAD_REQUEST);
		}
		Post post = postRepository.findById(postId).orElse(null);
		PostBody postBody = post == null ? null : new PostBody(post, currentUserId, new ProfileHead(userRepository.findById(post.getAuthorId()).orElse(null)));
		if (postBody == null) {
			getPostByIdResponse.setStatus(false);
			getPostByIdResponse.setMessage("Not post with this id");
			return Pair.of(getPostByIdResponse, HttpStatus.NOT_FOUND);
		} else {
			getPostByIdResponse.setStatus(true);
			getPostByIdResponse.setMessage("post found");
			getPostByIdResponse.setPostBody(postBody);
			return Pair.of(getPostByIdResponse, HttpStatus.OK);
		}
	}

	public Pair<DeletePostResponse, HttpStatus> deletePost(String postId, DeletePostRequest deletePostRequest) {
		DeletePostResponse deletePostResponse = new DeletePostResponse();
		if (postId == null || postId.trim().isEmpty()) {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("looking for a dead post?");
			return Pair.of(deletePostResponse, HttpStatus.BAD_REQUEST);
		}
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("why are you trying to delete something that does not exist");
			return Pair.of(deletePostResponse, HttpStatus.NOT_FOUND);
		}
		if (deletePostRequest.getCurrentUserId() == null || deletePostRequest.getCurrentUserId().trim().isEmpty()) {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("This place is for people who are alive, not ghosts.");
			return Pair.of(deletePostResponse, HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(deletePostRequest.getCurrentUserId()).orElse(null);
		boolean deletePermission = user != null && (post.getAuthorId().equals(user.getId()) || user.getRole() == UserRole.ADMIN);
		if (deletePermission) {
			postRepository.delete(post);
			commentRepository.deleteAllById(post.getCommentIDsOnThisPost());
			deletePostResponse.setStatus(true);
			deletePostResponse.setMessage("Post deleted successfully");
			return Pair.of(deletePostResponse, HttpStatus.ACCEPTED);
		} else {
			deletePostResponse.setStatus(false);
			deletePostResponse.setMessage("what do you think you are doing?");
			return Pair.of(deletePostResponse, HttpStatus.UNAUTHORIZED);
		}
	}

	//
	public Pair<EditPostResponse, HttpStatus> editPost(String postId, EditPostRequest editPostRequest) {
		EditPostResponse editPostResponse = new EditPostResponse();
		if (postId == null || postId.trim().isEmpty()) {
			editPostResponse.setStatus(false);
			editPostResponse.setMessage("looking for a dead post?");
			return Pair.of(editPostResponse, HttpStatus.BAD_REQUEST);
		}
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			editPostResponse.setStatus(false);
			editPostResponse.setMessage("You have schizophrenia?");
			return Pair.of(editPostResponse, HttpStatus.NOT_FOUND);
		}
		if (editPostRequest.getCurrentUserId() == null || editPostRequest.getCurrentUserId().trim().isEmpty()) {
			editPostResponse.setStatus(false);
			editPostResponse.setMessage("Hey Jack, we have a ghost here.");
			return Pair.of(editPostResponse, HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(editPostRequest.getCurrentUserId()).orElse(null);
		boolean editPermission = user != null && post.getAuthorId().equals(user.getId());
		if (!editPermission) {
			editPostResponse.setStatus(false);
			editPostResponse.setMessage("Are you sure you made this post.");
			return Pair.of(editPostResponse, HttpStatus.UNAUTHORIZED);
		}
		boolean text = post.getContentType() == PostContentType.TEXT;
		if (text && (editPostRequest.getPostData() == null || editPostRequest.getPostData().trim().isEmpty())) {
			editPostResponse.setStatus(false);
			editPostResponse.setMessage("We think it's better you delete this post instead of making a blank space.");
			return Pair.of(editPostResponse, HttpStatus.BAD_REQUEST);
		}
		if (text) post.setText(editPostRequest.getPostData());
		else post.setImageCaption(editPostRequest.getPostData());
		postRepository.save(post);
		editPostResponse.setStatus(true);
		editPostResponse.setMessage("Post updated.");
		return Pair.of(editPostResponse, HttpStatus.ACCEPTED);
	}

	public Pair<List<ProfileHead>, HttpStatus> getPostReactors(String postId) {
		if (postId == null || postId.trim().isEmpty())
			return Pair.of(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null)
			return Pair.of(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		else {
			List<ProfileHead> profileHeadList = new ArrayList<>();
			for (String userId : post.getUserIdsWhoLikedThisPost())
				profileHeadList.add(new ProfileHead(userRepository.findById(userId).orElse(null)));
			return Pair.of(profileHeadList, HttpStatus.OK);
		}
	}

	public Pair<List<PostBody>, HttpStatus> getPostsByUsername(String username, String currentUserId) {
		if (username == null || username.trim().isEmpty())
			return Pair.of(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		User user = userRepository.findByUsername(username).orElse(null);
		if (user == null) return Pair.of(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		List<Post> postsByUser = postRepository.findAllByAuthorId(user.getId());
		List<PostBody> postBodiesByUserList = new ArrayList<>();
		for (Post post : postsByUser)
			postBodiesByUserList.add(new PostBody(post, currentUserId, new ProfileHead(user)));
		return Pair.of(postBodiesByUserList, HttpStatus.OK);
	}

	public Pair<ReactResponse, HttpStatus> doReaction(String postId, ReactRequest reactRequest) {
		ReactResponse reactResponse = new ReactResponse();
		if (postId == null || postId.trim().isEmpty()) {
			reactResponse.setMessage("are you a friend of phineas and ferb?");
			reactResponse.setStatus(false);
			return Pair.of(reactResponse, HttpStatus.BAD_REQUEST);
		}
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) {
			reactResponse.setMessage("are you a friend of phineas and ferb?");
			reactResponse.setStatus(false);
			return Pair.of(reactResponse, HttpStatus.BAD_REQUEST);
		}
		if(reactRequest.getCurrentUserId()==null||reactRequest.getCurrentUserId().trim().isEmpty()){
			reactResponse.setStatus(false);
			reactResponse.setMessage("We are not calling an exorcist again.");
			return Pair.of(reactResponse,HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(reactRequest.getCurrentUserId()).orElse(null);
		if (user == null) {
			reactResponse.setStatus(false);
			reactResponse.setMessage("Please sign up to like this post.");
			return Pair.of(reactResponse,HttpStatus.UNAUTHORIZED);
		} else {
			reactResponse.setStatus(true);
			if (post.addReactor(reactRequest.getCurrentUserId())) reactResponse.setMessage("post liked.");
			else {
				post.deleteReactor(reactRequest.getCurrentUserId());
				reactResponse.setMessage("post unliked.");
			}
			postRepository.save(post);
			return Pair.of(reactResponse,HttpStatus.OK);
		}
	}
}
