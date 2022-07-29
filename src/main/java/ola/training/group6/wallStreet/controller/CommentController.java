package ola.training.group6.wallStreet.controller;

import mainpackage.socMedApp.model.comment.*;
import ola.training.group6.wallStreet.model.comment.*;
import ola.training.group6.wallStreet.service.CommentService;
import ola.trainingGroup6.wallStreet.model.comment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CommentController {
	@Autowired
	CommentService commentService;
	
	@PostMapping(value = "/comment", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CommentResponse> postComment(@RequestBody Comment comment) {
		Pair<CommentResponse, HttpStatus> commentResponse = commentService.postComment(comment);
		return new ResponseEntity<>(commentResponse.getFirst(), commentResponse.getSecond());
	}
	
	
	@DeleteMapping(value = "/comment/{commentId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable("commentId") String commentId, @RequestBody DeleteCommentRequest deleteCommentRequest) {
		Pair<DeleteCommentResponse, HttpStatus> deleteCommentResponse = commentService.deleteComment(commentId, deleteCommentRequest);
		return new ResponseEntity<>(deleteCommentResponse.getFirst(), deleteCommentResponse.getSecond());
	}
	
	@PutMapping(value = "/comment/{commentId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<EditCommentResponse> editComment(@PathVariable("commentId") String commentId, @RequestBody EditCommentRequest editCommentRequest) {
		Pair<EditCommentResponse, HttpStatus> editCommentResponse = commentService.editComment(commentId, editCommentRequest);
		return new ResponseEntity<>(editCommentResponse.getFirst(), editCommentResponse.getSecond());
	}
	
	@GetMapping(value = "/post/{postId}/comments")
	public ResponseEntity<List<CommentBanners>> getCommentsByPostId(@PathVariable("postId") String postId) {
		Pair<List<CommentBanners>, HttpStatus> commentBannersList = commentService.getCommentsByPostId(postId);
		return new ResponseEntity<>(commentBannersList.getFirst(), commentBannersList.getSecond());
	}
}
