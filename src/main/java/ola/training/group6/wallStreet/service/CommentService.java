package ola.training.group6.wallStreet.service;

import mainpackage.socMedApp.model.comment.*;
import ola.training.group6.wallStreet.model.comment.*;
import ola.trainingGroup6.wallStreet.model.comment.*;
import ola.training.group6.wallStreet.model.post.Post;
import ola.training.group6.wallStreet.model.user.ProfileHead;
import ola.training.group6.wallStreet.model.user.User;
import ola.training.group6.wallStreet.model.user.UserRole;
import ola.training.group6.wallStreet.repository.CommentRepository;
import ola.training.group6.wallStreet.repository.PostRepository;
import ola.training.group6.wallStreet.repository.UserRepository;
import ola.training.group6.wallStreet.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
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
	
	public Pair<CommentResponse, HttpStatus> postComment(Comment comment) {
		CommentResponse commentResponse = new CommentResponse();
		if (comment == null) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("invalid comment");
			return Pair.of(commentResponse, HttpStatus.BAD_REQUEST);
		}
		if (comment.getAuthorId() == null || comment.getAuthorId().trim().isEmpty()) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("We don't deal with ghosts.");
			return Pair.of(commentResponse, HttpStatus.UNAUTHORIZED);
		}
		if (!userRepository.existsById(comment.getAuthorId())) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("please sign up to make a comment.");
			return Pair.of(commentResponse, HttpStatus.UNAUTHORIZED);
		}
		if (comment.getPostId() == null || comment.getPostId().trim().isEmpty()) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("Where are you commenting dude.");
			return Pair.of(commentResponse, HttpStatus.BAD_REQUEST);
		}
		Post post = postRepository.findById(comment.getPostId()).orElse(null);
		if (post == null) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("Where are you commenting dude.");
			return Pair.of(commentResponse, HttpStatus.BAD_REQUEST);
		}
		if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("You sure you don't want to say something??");
			return Pair.of(commentResponse, HttpStatus.BAD_REQUEST);
		}
		if (comment.getTimestamp() == null || comment.getTimestamp() < post.getTimestamp()) {
			commentResponse.setStatus(false);
			commentResponse.setMessage("Dude, this is a social media website not time machine.");
			return Pair.of(commentResponse, HttpStatus.BAD_REQUEST);
		}
		
		String commentId;
		do commentId = Generator.idGen(); while (commentRepository.existsById(commentId));
		comment.setId(commentId);
		commentRepository.save(comment);
		post.addComment(commentId);
		postRepository.save(post);
		commentResponse.setCommentId(commentId);
		commentResponse.setStatus(true);
		commentResponse.setMessage("Your comment is saved.");
		
		return Pair.of(commentResponse, HttpStatus.CREATED);
	}
	
	public Pair<DeleteCommentResponse, HttpStatus> deleteComment(String commentId, DeleteCommentRequest deleteCommentRequest) {
		DeleteCommentResponse deleteCommentResponse = new DeleteCommentResponse();
		if (commentId == null || commentId.trim().isEmpty()) {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("How can you take back what you never said?");
			return Pair.of(deleteCommentResponse, HttpStatus.BAD_REQUEST);
		}
		Comment comment = commentRepository.findById(commentId).orElse(null);
		if (comment == null) {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("You have the wrong address man.");
			return Pair.of(deleteCommentResponse, HttpStatus.NOT_FOUND);
		}
		if (deleteCommentRequest.getCurrentUserId() == null || deleteCommentRequest.getCurrentUserId().trim().isEmpty()) {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("We don't remember providing services in hell.");
			return Pair.of(deleteCommentResponse, HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(deleteCommentRequest.getCurrentUserId()).orElse(null);
		Post post = postRepository.findById(comment.getPostId()).orElse(null);
		boolean deletePermission = user != null && post != null && (user.getRole() == UserRole.ADMIN || user.getId().equals(post.getAuthorId()) || user.getId().equals(comment.getAuthorId()));
		if (deletePermission) {
			commentRepository.delete(comment);
			post.deleteComment(commentId);
			postRepository.save(post);
			deleteCommentResponse.setStatus(true);
			deleteCommentResponse.setMessage("Comment deleted successfully.");
			return Pair.of(deleteCommentResponse, HttpStatus.ACCEPTED);
		} else {
			deleteCommentResponse.setStatus(false);
			deleteCommentResponse.setMessage("You are not the part of the trio who can destroy these words.");
			return Pair.of(deleteCommentResponse, HttpStatus.UNAUTHORIZED);
		}
	}
	
	public Pair<EditCommentResponse, HttpStatus> editComment(String commentId, EditCommentRequest editCommentRequest) {
		EditCommentResponse editCommentResponse = new EditCommentResponse();
		if (commentId == null || commentId.trim().isEmpty()) {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("The history we read has a lot of lies, but how can you change what was never said?");
			return Pair.of(editCommentResponse, HttpStatus.BAD_REQUEST);
		}
		Comment comment = commentRepository.findById(commentId).orElse(null);
		if (comment == null) {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("Man go to PK and get the right number for this comment.");
			return Pair.of(editCommentResponse, HttpStatus.NOT_FOUND);
		}
		if (editCommentRequest.getCurrentUserId() == null || editCommentRequest.getCurrentUserId().trim().isEmpty()) {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("Ghosts are getting really good with electronics these days. Oh! that makes sense.");
			return Pair.of(editCommentResponse, HttpStatus.UNAUTHORIZED);
		}
		User user = userRepository.findById(editCommentRequest.getCurrentUserId()).orElse(null);
		boolean editPermission = user != null && comment.getAuthorId().equals(user.getId());
		if (!editPermission) {
			editCommentResponse.setStatus(false);
			editCommentResponse.setMessage("You are not the speaker.");
			return Pair.of(editCommentResponse, HttpStatus.UNAUTHORIZED);
			
		} else {
			if (editCommentRequest.getCommentContent() == null || editCommentRequest.getCommentContent().trim().isEmpty()) {
				editCommentResponse.setStatus(false);
				editCommentResponse.setMessage("Man! you sure you want to edit this post not delete it?");
				return Pair.of(editCommentResponse, HttpStatus.BAD_REQUEST);
			}
			comment.setContent(editCommentRequest.getCommentContent());
			commentRepository.save(comment);
			editCommentResponse.setStatus(true);
			editCommentResponse.setMessage("comment updated");
			return Pair.of(editCommentResponse, HttpStatus.ACCEPTED);
		}
	}
	
	public Pair<List<CommentBanners>, HttpStatus> getCommentsByPostId(String postId) {
		if (postId == null || postId.trim().isEmpty()) return Pair.of(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		Post post = postRepository.findById(postId).orElse(null);
		if (post == null) return Pair.of(new ArrayList<>(), HttpStatus.NOT_FOUND);
		List<CommentBanners> commentBannersList = new ArrayList<>();
		List<Comment> commentList = (List<Comment>) commentRepository.findAllById(post.getCommentIDsOnThisPost());
		for (Comment comment : commentList)
			commentBannersList.add(new CommentBanners(comment, new ProfileHead(userRepository.findById(comment.getAuthorId()).orElse(null))));
		return Pair.of(commentBannersList, HttpStatus.OK);
	}
}

