package mainpackage.socMedApp.model;

public class SignUpResponse {
	private String message;
	private boolean status;
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

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
