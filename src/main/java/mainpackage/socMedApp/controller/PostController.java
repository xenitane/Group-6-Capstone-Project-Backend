package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.post.DeletePostRequest;
import mainpackage.socMedApp.model.ReactResponse;
import mainpackage.socMedApp.model.post.*;
import mainpackage.socMedApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PostController {
	@Autowired
	PostService postService;

	@PostMapping(value = "/post", consumes = "application/json")
	public ResponseEntity<PostResponse> makePost(@RequestBody Post post) {
		PostResponse postResponse = postService.savePost(post);
		return new ResponseEntity<>(postResponse, postResponse.isStatus() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/post/{postId}")
	public ResponseEntity<GetPostByIdResponse> getPost(@PathVariable String postId, @RequestHeader("currentUserId") String currentUserId) {
		GetPostByIdResponse getPostByIdResponse = postService.getPostById(postId, currentUserId);
		return new ResponseEntity<>(getPostByIdResponse, getPostByIdResponse.isStatus() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/post/{postId}")
	public ResponseEntity<DeletePostResponse> deleteProduct(@PathVariable String postId, @RequestBody DeletePostRequest deletePostRequest) {
		DeletePostResponse deletePostResponse = postService.deletePost(postId, deletePostRequest);
		return new ResponseEntity<>(deletePostResponse, deletePostResponse.isStatus() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
	}

	@PutMapping(value = "/post/{postId}")
	public ResponseEntity<EditPostResponse> editPost(@PathVariable("postId") String postId, @RequestBody EditPostRequest editPostRequest) {
		EditPostResponse editPostResponse = postService.editPost(postId, editPostRequest);
		return new ResponseEntity<>(editPostResponse, editPostResponse.isStatus() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
	}

	@GetMapping(value = "/{username}/posts")
	public ResponseEntity<List<PostBody>> getPostsByUsername(@PathVariable("username") String username, @RequestHeader("currentUserId") String currentUserId) {
		List<PostBody> postBodyList = postService.getPostsByUsername(username, currentUserId);
		return new ResponseEntity<>(postBodyList, postBodyList == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@PutMapping(value = "/post/{postId}/react")
	public ResponseEntity<ReactResponse> reactOnPost(@PathVariable("postId") String postId, @RequestHeader("currentUserId") String currentUserId) {
		ReactResponse reactResponse = postService.doReaction(postId, currentUserId);
		return new ResponseEntity<>(reactResponse, reactResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}
