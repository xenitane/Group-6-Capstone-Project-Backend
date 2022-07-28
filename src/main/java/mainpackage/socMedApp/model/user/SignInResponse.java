package mainpackage.socMedApp.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponse {
	private boolean status;
	private String message;
	private String userId;
}
