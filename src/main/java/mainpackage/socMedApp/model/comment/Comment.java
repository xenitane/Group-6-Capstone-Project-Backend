package mainpackage.socMedApp.model.comment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("Comments")
public class Comment {
	@Id
	private String id;
	private String content;
	private String authorId;
	private String postId;
	private Long timeStamp;
}
