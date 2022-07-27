package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.Comment;
import mainpackage.socMedApp.model.CommentResponse;
import mainpackage.socMedApp.model.DeleteCommentRequest;
import mainpackage.socMedApp.model.EditCommentRequest;
import mainpackage.socMedApp.repository.CommentRepository;
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

            commentRepository.save(comment);
            //todo -- in postrepo update comment array

        }
        return commentResponse;
    }

    public void delete(DeleteCommentRequest deleteCommentRequest) {
        /* return deletecommentresponse
         */

        Optional<Comment> optionalComment = commentRepository.findById(deleteCommentRequest.getCommentId());
        if (optionalComment.isEmpty()) {
/* to do response data and exception handling*/
        } else {
            if (optionalComment.get().getAuthorId().equals(deleteCommentRequest.getAuthorId())) {
                commentRepository.deleteById(deleteCommentRequest.getCommentId());
            }
        }
    }

    public void edit_comment(EditCommentRequest editCommentRequest) {
        /* return deletecommentresponse
         */

//        Optional<Comment> optionalComment1 = commentRepository.findById(editCommentRequest.getCommentId());
        Comment comment=commentRepository.findById(editCommentRequest.getCommentId()).orElse(null);
        if (comment==null) {
            /*
             * todo response */
        } else {
            if(comment.getAuthorId().equals(editCommentRequest.getUserId())) {
                comment.setContent(editCommentRequest.getCommentData());
                commentRepository.save(comment);
            }else{

            }
        }
    }


    }

