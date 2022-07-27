package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.comment.*;
import mainpackage.socMedApp.model.post.Post;
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
import java.util.List;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	public CommentResponse postComment(Comment comment) {
		CommentResponse commentResponse = new CommentResponse();
		if (comment == null || comment.getTimeStamp() == null) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("invalid comment");
			return commentResponse;
		}
		if (!userRepository.existsById(comment.getAuthorId())) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("Sign up to make a comment.");
			return commentResponse;
		}
		Post post = postRepository.findById(comment.getPostId()).orElse(null);
		if (post == null) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("Where are you commenting dude.");
			return commentResponse;
		}
		if (comment.getContent() == null || comment.getContent().length() == 0) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("invalid comment.");
		} else {
			String commentId;
			do commentId = Generator.idGen(); while (commentRepository.existsById(commentId));
			comment.setId(commentId);
			post.addComment(commentId);
			postRepository.save(post);
			commentRepository.save(comment);
			commentResponse.setCommentId(commentId);
			commentResponse.setStatus(true);
			commentResponse.setMessage("Your comment is saved.");
		}
		return commentResponse;
	}

	public DeleteCommentResponse deleteComment(String commentId, DeleteCommentRequest deleteCommentRequest) {
		DeleteCommentResponse deleteCommentResponse = new DeleteCommentResponse();
		Comment comment = commentRepository.findById(commentId).orElse(null);
		if (comment == null) {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("Comment not found.");
			return deleteCommentResponse;
		}
		User user = userRepository.findById(deleteCommentRequest.getCurrentUserId()).orElse(null);
		Post post = postRepository.findById(comment.getPostId()).orElse(null);
		boolean deletePermission = user != null && post != null && (user.getRole() == UserRole.ADMIN || user.getId().equals(post.getAuthorId()) || user.getId().equals(comment.getAuthorId()));
		if (deletePermission) {
			commentRepository.delete(comment);
			post.deleteComment(commentId);
			postRepository.save(post);
			deleteCommentResponse.setStatus(true);
			deleteCommentResponse.setMessage("Comment deleted successfully");
		} else {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("You don't have permission to delete this comment");
		}
		return deleteCommentResponse;
	}

	public EditCommentResponse editComment(String commentId, EditCommentRequest editCommentRequest) {
		EditCommentResponse editCommentResponse = new EditCommentResponse();
		Comment comment = commentRepository.findById(commentId).orElse(null);
		if (comment == null) {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("comment not found");
			return editCommentResponse;
		}
		User user = userRepository.findById(editCommentRequest.getCurrentUserId()).orElse(null);
		boolean editPermission = user != null && comment.getAuthorId().equals(user.getId());
		if (editPermission) {
			comment.setContent(editCommentRequest.getCommentContent());
			commentRepository.save(comment);
			editCommentResponse.setStatus(true);
			editCommentResponse.setMessage("comment updated");
		} else {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("lack of permission");
		}
		return editCommentResponse;
	}

	public List<CommentBanners> getCommentsByPostId(String postId) {
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) return new ArrayList<>();
		List<CommentBanners> commentBannersList = new ArrayList<>();
		List<Comment> commentList = (List<Comment>) commentRepository.findAllById(post.getCommentIDsOnThisPost());
		for (Comment comment : commentList)
			commentBannersList.add(new CommentBanners(comment, new ProfileHead(userRepository.findById(comment.getAuthorId()).orElse(null))));
		return commentBannersList;
	}
}

