package mainpackage.socMedApp.repository;

import mainpackage.socMedApp.model.comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
}
