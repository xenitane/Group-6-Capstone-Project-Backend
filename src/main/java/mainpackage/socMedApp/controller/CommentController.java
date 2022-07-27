package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.Comment;
import mainpackage.socMedApp.model.CommentResponse;
import mainpackage.socMedApp.model.DeleteCommentRequest;

import mainpackage.socMedApp.repository.CommentRepository;
import mainpackage.socMedApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping(value = "/post_comment",consumes = "application/json",produces = "application/json")
    public CommentResponse postComment(@RequestBody Comment comment){

        CommentResponse commentResponse = commentService.register(comment);
        return commentResponse;

    }


    @DeleteMapping(value = "/delete_comment",consumes = "application/json",produces = "application/json")
    public void deletecomment(@RequestBody DeleteCommentRequest deleteCommentRequest){
        commentService.delete(deleteCommentRequest);

    }


    }

