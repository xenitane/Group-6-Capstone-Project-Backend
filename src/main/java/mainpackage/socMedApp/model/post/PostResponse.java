package mainpackage.socMedApp.model.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {

    private boolean status;
    private String message;
    private String postId;
}
