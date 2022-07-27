package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.*;
import mainpackage.socMedApp.repository.CommentRepository;
import mainpackage.socMedApp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public CommentResponse register(Comment comment){

        CommentResponse commentResponse= new CommentResponse();
        if(comment==null){
            commentResponse.setCommentStatus(400);
            commentResponse.setCommentMessage("please enter valid comment");

        }
        else{
            commentResponse.setCommentStatus(201);
            commentResponse.setCommentMessage("comment posted");
            String commentID;
            do{
                commentID= Generator.idGen();
            }while(commentRepository.existsById(commentID));
            commentRepository.save(comment);
            //todo -- in postrepo update comment array

        }
        return commentResponse;
    }

    public void delete(DeleteCommentRequest deleteCommentRequest) {
        /* return deletecommentresponse
         */
        DeleteCommentResponse deleteCommentResponse= new DeleteCommentResponse();

        Optional<Comment> optionalComment = commentRepository.findById(deleteCommentRequest.getCommentId());
        if (optionalComment.isEmpty()) {
/* to do response data and exception handling*/
            deleteCommentResponse.setDeleteCommentStatus(400);
            deleteCommentResponse.getDeleteCommentMessage("Comment not found");

        } else {
            if (optionalComment.get().getAuthorId().equals(deleteCommentRequest.getAuthorId())) {
                commentRepository.deleteById(deleteCommentRequest.getCommentId());

                deleteCommentResponse.setDeleteCommentStatus(201);
                deleteCommentResponse.setDeleteCommentMessage("Comment deleted successfully");
            }
        }
    }

    public EditCommentResponse edit_comment(EditCommentRequest editCommentRequest) {
        /* return deletecommentresponse
         */
        EditCommentResponse editCommentResponse= new EditCommentResponse();

        Comment comment=commentRepository.findById(editCommentRequest.getCommentId()).orElse(null);
        if (comment==null) {
            editCommentResponse.setEditCommentStatus(400);
            editCommentResponse.setEditCommentMessage("No existing comment to edit");
        } else {
            if(comment.getAuthorId().equals(editCommentRequest.getUserId())) {

                comment.setContent(editCommentRequest.getCommentData());
                commentRepository.save(comment);

                editCommentResponse.setEditCommentStatus(201);
                editCommentResponse.setEditCommentMessage("Comment successfully updated");
            }
        }
    }


    }

