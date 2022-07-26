package mainpackage.socMedApp.repository;

import mainpackage.socMedApp.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    Post findByPostId(String postId);
}
