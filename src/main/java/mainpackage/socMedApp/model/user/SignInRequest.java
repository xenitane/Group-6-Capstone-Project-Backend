package mainpackage.socMedApp.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
	private String cred;
	private String password;
}
