package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.GetPostByIdResponse;
import mainpackage.socMedApp.model.Post;
import mainpackage.socMedApp.model.PostResponse;
import mainpackage.socMedApp.service.PostService;
import mainpackage.socMedApp.util.Generator;
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

    @GetMapping(value = "/post/{postId}")
    public GetPostByIdResponse getPost(@PathVariable String postId){

        return postService.getPostById(postId);

    }
}
