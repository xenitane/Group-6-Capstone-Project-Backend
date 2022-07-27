package mainpackage.socMedApp.model;

public class DeleteCommentResponse {
    private String deleteCommentMessage;
    private long deleteCommentStatus; // 201 success   400 failure

    public String getDeleteCommentMessage() {
        return deleteCommentMessage;
    }

    public void setDeleteCommentMessage(String deleteCommentMessage) {
        this.deleteCommentMessage = deleteCommentMessage;
    }

    public long getDeleteCommentStatus() {
        return deleteCommentStatus;
    }

    public void setDeleteCommentStatus(long deleteCommentStatus) {
        this.deleteCommentStatus = deleteCommentStatus;
    }
}
