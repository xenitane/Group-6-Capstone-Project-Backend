package ola.training.group6.wallStreet.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileHeadResponse {
	private boolean status;
	private String message;
	private ProfileHead profileHead;
}
