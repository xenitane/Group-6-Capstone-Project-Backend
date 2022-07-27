package mainpackage.socMedApp.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mainpackage.socMedApp.model.user.ProfileHead;

@Getter
@Setter
@AllArgsConstructor
public class CommentBanners {
	private Comment comment;
	private ProfileHead profileHead;
}
