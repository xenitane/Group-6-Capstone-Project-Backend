package mainpackage.socMedApp.model;

public class SignInResponse {
	private boolean status;
	private String message;
	private String userID;

	public String getMessage() {
		return message;
	}

	public String getUserID() {
		return userID;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
