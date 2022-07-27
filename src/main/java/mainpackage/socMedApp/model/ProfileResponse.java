package mainpackage.socMedApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
	private boolean status;
	private String Message;
	private Profile profile;
}
