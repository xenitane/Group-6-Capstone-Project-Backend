package ola.training.group6.wallStreet.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponse {
	private boolean status;
	private String message;
	private String userId;
	private String username;
	private String displayPictureURI;
}
