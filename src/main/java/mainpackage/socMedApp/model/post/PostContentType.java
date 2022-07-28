package mainpackage.socMedApp.model.post;

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
