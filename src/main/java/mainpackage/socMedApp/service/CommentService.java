package mainpackage.socMedApp.service;

import mainpackage.socMedApp.model.Comment;
import mainpackage.socMedApp.model.CommentResponse;
import mainpackage.socMedApp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
