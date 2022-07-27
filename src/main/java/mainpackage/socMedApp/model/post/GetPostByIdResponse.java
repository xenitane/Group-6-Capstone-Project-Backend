package mainpackage.socMedApp.model.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPostByIdResponse {
    private boolean status;
    private String message;
    private PostBody postBody;
}
