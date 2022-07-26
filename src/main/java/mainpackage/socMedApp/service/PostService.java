package mainpackage.socMedApp.service;


import mainpackage.socMedApp.model.GetPostByIdResponse;
import mainpackage.socMedApp.model.Post;
import mainpackage.socMedApp.model.PostResponse;
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
                postResponse.setResponseMessage("Post created successfully");

                return postResponse;
            } else {
                PostResponse postResponse = new PostResponse();
                postResponse.setHttpStatus(404);
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
                postByIdResponse.setPost_data(post);
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

}
