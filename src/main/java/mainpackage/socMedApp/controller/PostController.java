package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping(value = "/post", consumes = "application/json")
    public PostResponse post(@RequestBody Post post){

        PostResponse postResponse = postService.savePost(post);

        return postResponse;
    }

    @GetMapping(value = "/get_post/{postId}")
    public GetPostByIdResponse getPost(@PathVariable String postId){
        return postService.getPostById(postId);
    }

    @DeleteMapping(value = "/delete_post/{postId}")
    public DeletePostResponse deleteProduct(@PathVariable String postId) {
        return postService.deletePost(postId);
    }

//    @PutMapping(value = "/edit_post")
//    public EditPostResponse EditPost(@RequestBody Post post) {
//        return postService.editPost(post);
//    }
@PutMapping(value = "/edit_post/{postId}")
public EditPostResponse EditPost(@PathVariable String postId, @RequestBody Post post) {
    return postService.editPost(postId,post);
}

}
