package mainpackage.socMedApp.model;

public class DeletePostResponse {
    private long httpStatus;
    private String responseMessage;

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
