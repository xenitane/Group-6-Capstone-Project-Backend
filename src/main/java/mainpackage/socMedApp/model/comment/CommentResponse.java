package mainpackage.socMedApp.model.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
    private String message;
    private boolean status;
    private String commentId;

}
