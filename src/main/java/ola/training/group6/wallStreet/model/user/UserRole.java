package ola.training.group6.wallStreet.model.user;

public enum UserRole {
	END_USER(0), ADMIN(1);
	private final int value;

	public int value() {
		return this.value;
	}

	private UserRole(int value) {
		this.value = value;
	}
}
