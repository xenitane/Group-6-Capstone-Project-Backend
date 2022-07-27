package mainpackage.socMedApp.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileHead {
	private String id;
	private String username;
	private String displayPictureURI;

	public ProfileHead(User user) {
		if (user != null) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.displayPictureURI = user.getDisplayPictureURI();
		}
	}
}
