package ola.training.group6.wallStreet.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileHead {
	private String username;
	private String displayPictureURI;

	public ProfileHead(User user) {
		if (user != null) {
			this.username = user.getUsername();
			this.displayPictureURI = user.getDisplayPictureURI();
		}
	}
}
