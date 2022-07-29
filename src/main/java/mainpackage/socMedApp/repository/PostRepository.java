package mainpackage.socMedApp.repository;

import mainpackage.socMedApp.model.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
	List<Post> findAllByAuthorId(String authorId);
}
