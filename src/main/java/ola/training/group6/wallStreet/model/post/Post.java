package ola.training.group6.wallStreet.model.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document("posts")
@Getter
@Setter
public class Post {
	@Id
	private String id;
	private String authorId;
	private Long timestamp;
	private PostContentType contentType;
	private String imageURL;
	private String imageCaption;
	private String text;
	private Integer likeCount;
	private Integer commentCount;
	private HashSet<String> userIdsWhoLikedThisPost;
	private HashSet<String> commentIDsOnThisPost;

	public boolean addReactor(String reactorId) {
		if (userIdsWhoLikedThisPost.add(reactorId)) {
			this.likeCount++;
			return true;
		}
		return false;
	}

	public void deleteReactor(String reactorId) {
		if (userIdsWhoLikedThisPost.remove(reactorId)) {
			this.likeCount--;
		}
	}

	public void addComment(String commentId) {
		if (commentIDsOnThisPost.add(commentId)) {
			this.commentCount++;
		}
	}

	public void deleteComment(String commentId) {
		if (commentIDsOnThisPost.remove(commentId)) {
			this.commentCount--;
		}
	}
}
