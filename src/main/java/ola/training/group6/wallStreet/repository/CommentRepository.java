package ola.training.group6.wallStreet.repository;

import ola.training.group6.wallStreet.model.comment.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
}
