package mainpackage.socMedApp.model.post;

import lombok.Getter;
import lombok.Setter;
import mainpackage.socMedApp.model.user.ProfileHead;

@Getter
@Setter
public class PostBody {
	private String id;
	private Long timestamp;
	private PostContentType contentType;
	private String imageURL;
	private String imageCaption;
	private String text;
	private Integer likeCount;
	private Integer commentCount;
	private boolean isLikedByCurrentUser;
	private ProfileHead profileHead;

	public PostBody(Post post, String currentUserId, ProfileHead profileHead) {
		if (post != null) {
			this.id = post.getId();
			this.timestamp = post.getTimestamp();
			this.commentCount = post.getCommentCount();
			this.contentType = post.getContentType();
			this.imageURL = post.getImageURL();
			this.imageCaption = post.getImageCaption();
			this.text = post.getText();
			this.likeCount = post.getLikeCount();
			this.commentCount = post.getCommentCount();
			this.isLikedByCurrentUser = post.getUserIdsWhoLikedThisPost().contains(currentUserId);
			this.profileHead = profileHead;
		}
	}
}
