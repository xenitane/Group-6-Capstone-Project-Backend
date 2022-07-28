package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.comment.*;
import mainpackage.socMedApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
	@Autowired
	CommentService commentService;

	@PostMapping(value = "/comment", consumes = "application/json", produces = "application/json")
	public ResponseEntity<CommentResponse> postComment(@RequestBody Comment comment) {
		CommentResponse commentResponse = commentService.postComment(comment);
		return new ResponseEntity<>(commentResponse, commentResponse.isStatus() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
	}


	@DeleteMapping(value = "/comment/{commentId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable("commentId") String commentId, @RequestBody DeleteCommentRequest deleteCommentRequest) {
		DeleteCommentResponse deleteCommentResponse = commentService.deleteComment(commentId, deleteCommentRequest);
		return new ResponseEntity<>(deleteCommentResponse, deleteCommentResponse.isStatus() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
	}

	@PutMapping(value = "/comment/{commentId}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<EditCommentResponse> editComment(@PathVariable("commentId") String commentId, @RequestBody EditCommentRequest editCommentRequest) {
		EditCommentResponse editCommentResponse = commentService.editComment(commentId, editCommentRequest);
		return new ResponseEntity<>(editCommentResponse, editCommentResponse.isStatus() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
	}

	@GetMapping(value = "/post/{postId}/comments")
	public ResponseEntity<List<CommentBanners>> getCommentsByPostId(@PathVariable("postId") String postId) {
		List<CommentBanners> commentBannersList = commentService.getCommentsByPostId(postId);
		return new ResponseEntity<>(commentBannersList, commentBannersList == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
}
