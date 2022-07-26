package mainpackage.socMedApp.model;

public class UserResponse {
	private boolean status;
	private String message;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getDisplayPictureURI() {
		return displayPictureURI;
	}

	public void setDisplayPictureURI(String displayPictureURI) {
		this.displayPictureURI = displayPictureURI;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getMessage() {
		return message;
	}

	private String id;
	private String name;
	private String username;
	private String bio;
	private String displayPictureURI;
	private UserRole role;

	public UserResponse(User user) {
		super();
		if (user != null) {
			this.id = user.getId();
			this.name = user.getName();
			this.username = user.getUsername();
			this.bio = user.getBio();
			this.displayPictureURI = user.getDisplayPictureURI();
			this.role = user.getRole();
			this.status = true;
			this.message = "User found.";
		} else {
			this.status = false;
			this.message = "user not found.";
		}
	}
}
