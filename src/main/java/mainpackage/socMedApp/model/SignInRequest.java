package mainpackage.socMedApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
	private String cred;
	private String password;
}
