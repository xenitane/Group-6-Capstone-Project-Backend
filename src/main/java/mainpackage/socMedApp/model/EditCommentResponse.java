package mainpackage.socMedApp.model;

public class EditCommentResponse {
    private String editCommentMessage;
    private long editCommentStatus; // 201 success   400 failure


    public String getEditCommentMessage() {
        return editCommentMessage;
    }

    public void setEditCommentMessage(String editCommentMessage) {
        this.editCommentMessage = editCommentMessage;
    }

    public long getEditCommentStatus() {
        return editCommentStatus;
    }

    public void setEditCommentStatus(long editCommentStatus) {
        this.editCommentStatus = editCommentStatus;
    }
}
