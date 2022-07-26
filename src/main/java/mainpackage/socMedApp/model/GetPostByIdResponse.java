package mainpackage.socMedApp.model;

import java.util.List;

public class GetPostByIdResponse {

    private Post post_data;
    private List<String> commentIds;
    private long timestamp;

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GetPostByIdResponse{" +
                "post_data=" + post_data +
                ", commentIds=" + commentIds +
                ", timestamp=" + timestamp +
                '}';
    }

    public Post getPost_data() {
        return post_data;
    }

    public void setPost_data(Post post_data) {
        this.post_data = post_data;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
