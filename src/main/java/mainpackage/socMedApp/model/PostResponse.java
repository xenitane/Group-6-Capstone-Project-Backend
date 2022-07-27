package mainpackage.socMedApp.model;

public class PostResponse {

    private long httpStatus;
    private String responseMessage;

    private String postId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }



    public long getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(long httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
