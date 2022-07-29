package ola.training.group6.wallStreet.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
	private String username;
	private String displayPictureURI;
	private String bio;
	private String name;
	private UserRole role;

	public Profile(User user) {
		if (user != null) {
			this.username = user.getUsername();
			this.displayPictureURI = user.getDisplayPictureURI();
			this.bio = user.getBio();
			this.name = user.getName();
			this.role = user.getRole();
		}
	}
}
