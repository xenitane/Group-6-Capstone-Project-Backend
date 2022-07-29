package ola.training.group6.wallStreet.model.post;

public enum PostContentType {
	TEXT(0), IMAGE(1);
	private final int value;

	private PostContentType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
