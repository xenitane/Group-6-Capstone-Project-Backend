package ola.training.group6.wallStreet.model.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
public class User {
	@Id
	private String id;
	private String username;
	private String displayPictureURI;
	private String bio;
	private String name;
	private UserRole role;
	private String email;
	private String password;
	private String salt;
}
