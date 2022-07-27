package mainpackage.socMedApp.service;


import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.repository.PostRepository;
import mainpackage.socMedApp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public PostResponse savePost(Post post){

        try{
            String postID=Generator.IDGen();

            while(postRepository.existsById(postID))postID=Generator.IDGen();

            post.setPostId(postID);
            Post savedPost = postRepository.save(post);

            if (savedPost != null){
                PostResponse postResponse = new PostResponse();
                postResponse.setHttpStatus(201);
                postResponse.setPostId(postID);
                postResponse.setResponseMessage("Post created successfully");

                return postResponse;
            } else {
                PostResponse postResponse = new PostResponse();
                postResponse.setHttpStatus(500);
                postResponse.setResponseMessage("Error in creating post");

                return postResponse;
            }
        } catch (Exception e){
            PostResponse postResponse = new PostResponse();
            postResponse.setHttpStatus(404);
            postResponse.setResponseMessage("Error in creating post : " + e);

            return postResponse;
        }


    }


    public GetPostByIdResponse getPostById(String postId){
        try {
            Post post = postRepository.findByPostId(postId);
            GetPostByIdResponse postByIdResponse = new GetPostByIdResponse();
            postByIdResponse.setStatus("200");
            if (post == null){
                postByIdResponse.setMessage("No post found");
            } else {
                postByIdResponse.setPostData(post);
                postByIdResponse.setCommentIds(post.getCommentIdsWhoCommentedThisPost());
                postByIdResponse.setTimestamp(post.getTimestamp());
                postByIdResponse.setMessage("Post found");
            }

            return postByIdResponse;

        } catch (Exception e){
            GetPostByIdResponse postByIdResponse = new GetPostByIdResponse();
            postByIdResponse.setStatus("404");
            postByIdResponse.setMessage("Error in getting post by id");
            return postByIdResponse;
        }



    }

    public DeletePostResponse deletePost(String postId) {
        Post post = postRepository.deleteByPostId(postId);
        DeletePostResponse deletePostResponse = new DeletePostResponse();
        if(post==null){
            deletePostResponse.setResponseMessage("No post found!");
            deletePostResponse.setHttpStatus(404);
            return deletePostResponse;
        }else{
            deletePostResponse.setHttpStatus(200);
            deletePostResponse.setResponseMessage("post deleted successfully!");
            return deletePostResponse;
        }
    }

//
public EditPostResponse editPost(String postId, Post post) {
    EditPostResponse editPostResponse = new EditPostResponse();
    Post existingPost = postRepository.findById(postId).orElse(null);
    Optional<Post> author = postRepository.findById(post.getUserId());
    if(existingPost == null){
        editPostResponse.setResponseMessage("No post found");
        editPostResponse.setHttpStatus(404);
        return editPostResponse;

    }else {
        existingPost.setCaption(post.getCaption());
        existingPost.setText(post.getText());
        postRepository.save(existingPost);
        editPostResponse.setHttpStatus(200);
        editPostResponse.setResponseMessage("post updated successfully!");
        return editPostResponse;
    }

}

}
