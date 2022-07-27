package mainpackage.socMedApp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
	private String username;
	private String name;
	private String bio;
	private String displayPictureURI;
	private UserRole role;
}
