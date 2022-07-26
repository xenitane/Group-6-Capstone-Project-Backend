package mainpackage.socMedApp.model;

public class SignInRequest {
	private String cred;
	private String password;

	@Override
	public String toString() {
		return "SignInRequest{" +
				"cred='" + cred + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	public String getCred() {
		return cred;
	}

	public void setCred(String cred) {
		this.cred = cred;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
