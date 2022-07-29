package ola.training.group6.wallStreet.repository;

import ola.training.group6.wallStreet.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
}
