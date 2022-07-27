package mainpackage.socMedApp.controller;

import mainpackage.socMedApp.model.comment.Comment;
import mainpackage.socMedApp.model.comment.CommentResponse;
import mainpackage.socMedApp.model.comment.DeleteCommentRequest;
import mainpackage.socMedApp.model.comment.EditCommentRequest;
import mainpackage.socMedApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping(value = "/post_comment", consumes = "application/json", produces = "application/json")
    public CommentResponse postComment(@RequestBody Comment comment) {

        CommentResponse commentResponse = commentService.register(comment);
        return commentResponse;

    }


    @DeleteMapping(value = "/delete_comment", consumes = "application/json", produces = "application/json")
    public void deletecomment(@RequestBody DeleteCommentRequest deleteCommentRequest) {
        commentService.delete(deleteCommentRequest);

    }

    @PutMapping(value = "/edit_comment", consumes = "application/json", produces = "application/json")
    public void editcomment(@RequestBody EditCommentRequest editCommentRequest) {
        commentService.edit_comment(editCommentRequest);

    }
}
