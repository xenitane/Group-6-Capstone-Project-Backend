package mainpackage.socMedApp.model;

public class CommentResponse {
    private String commentMessage;
    private long commentStatus; // 201 success   400 failure

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public long getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(long commentStatus) {
        this.commentStatus = commentStatus;
    }
}
