package mainpackage.socMedApp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpResponse {
	private String message;
	private boolean status;
	private String userID;
}
