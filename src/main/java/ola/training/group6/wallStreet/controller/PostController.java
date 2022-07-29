package ola.training.group6.wallStreet.controller;

import ola.training.group6.wallStreet.model.post.*;
import ola.training.group6.wallStreet.model.user.ProfileHead;
import ola.training.group6.wallStreet.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class PostController {
	@Autowired
	PostService postService;
	
	@PostMapping(value = "/post", consumes = "application/json")
	public ResponseEntity<PostResponse> makePost(@RequestBody Post post) {
		Pair<PostResponse, HttpStatus> postResponse = postService.savePost(post);
		return new ResponseEntity<>(postResponse.getFirst(), postResponse.getSecond());
	}
	
	@GetMapping(value = "/post/{postId}")
	public ResponseEntity<GetPostByIdResponse> getPost(@PathVariable String postId, @Nullable @RequestHeader("currentUserId") String currentUserId) {
		Pair<GetPostByIdResponse, HttpStatus> getPostByIdResponse = postService.getPostById(postId, currentUserId);
		return new ResponseEntity<>(getPostByIdResponse.getFirst(), getPostByIdResponse.getSecond());
	}
	
	@DeleteMapping(value = "/post/{postId}")
	public ResponseEntity<DeletePostResponse> deletePost(@PathVariable String postId, @RequestBody DeletePostRequest deletePostRequest) {
		Pair<DeletePostResponse, HttpStatus> deletePostResponse = postService.deletePost(postId, deletePostRequest);
		return new ResponseEntity<>(deletePostResponse.getFirst(), deletePostResponse.getSecond());
	}
	
	@PutMapping(value = "/post/{postId}")
	public ResponseEntity<EditPostResponse> editPost(@PathVariable("postId") String postId, @RequestBody EditPostRequest editPostRequest) {
		Pair<EditPostResponse, HttpStatus> editPostResponse = postService.editPost(postId, editPostRequest);
		return new ResponseEntity<>(editPostResponse.getFirst(), editPostResponse.getSecond());
	}
	
	@GetMapping(value = "/user/{username}/posts")
	public ResponseEntity<List<PostBody>> getPostsByUsername(@PathVariable("username") String username, @Nullable @RequestHeader("currentUserId") String currentUserId) {
		Pair<List<PostBody>, HttpStatus> postBodyList = postService.getPostsByUsername(username, currentUserId);
		return new ResponseEntity<>(postBodyList.getFirst(), postBodyList.getSecond());
	}
	
	@GetMapping(value = "/feed")
	public ResponseEntity<List<PostBody>> getFeed(@Nullable @RequestHeader("currentUserId") String currentUserId) {
		Pair<List<PostBody>, HttpStatus> feed = postService.getFeed(currentUserId);
		return new ResponseEntity<>(feed.getFirst(), feed.getSecond());
	}
	
	@PatchMapping(value = "/post/{postId}/react")
	public ResponseEntity<ReactResponse> reactOnPost(@PathVariable("postId") String postId, @RequestBody ReactRequest reactRequest) {
		Pair<ReactResponse, HttpStatus> reactResponse = postService.doReaction(postId, reactRequest);
		return new ResponseEntity<>(reactResponse.getFirst(), reactResponse.getSecond());
	}
	
	@GetMapping(value = "/post/{postId}/reactors")
	public ResponseEntity<List<ProfileHead>> getReactorsByPostId(@PathVariable("postId") String postId) {
		Pair<List<ProfileHead>, HttpStatus> profileHeadList = postService.getPostReactors(postId);
		return new ResponseEntity<>(profileHeadList.getFirst(), profileHeadList.getSecond());
	}
}

