package mainpackage.socMedApp.model.comment;

import lombok.Getter;
import lombok.Setter;
import mainpackage.socMedApp.model.user.ProfileHead;

@Getter
@Setter
public class CommentBanners {
	private String id;
	private String content;
	private String postId;
	private Long timeStamp;
	private ProfileHead profileHead;

	public CommentBanners(Comment comment, ProfileHead profileHead) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.postId = comment.getPostId();
		this.timeStamp = comment.getTimeStamp();
		this.profileHead = profileHead;
	}
}
