package mainpackage.socMedApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("posts")
public class Post {

    @Id
    private String postId;
    private String userId;
    private long timestamp;
    private String contentType;
    private String url;
    private String caption;
    private String text;
    private int likeCount;
    private int commentCount;

    private List<String> userIdsWhoLikedThisPost;
    private List<String> commentIdsWhoCommentedThisPost;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getUserIdsWhoLikedThisPost() {
        return userIdsWhoLikedThisPost;
    }

    public void setUserIdsWhoLikedThisPost(List<String> userIdsWhoLikedThisPost) {
        this.userIdsWhoLikedThisPost = userIdsWhoLikedThisPost;
    }

    public List<String> getCommentIdsWhoCommentedThisPost() {
        return commentIdsWhoCommentedThisPost;
    }

    public void setCommentIdsWhoCommentedThisPost(List<String> commentIdsWhoCommentedThisPost) {
        this.commentIdsWhoCommentedThisPost = commentIdsWhoCommentedThisPost;
    }



    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                ", caption='" + caption + '\'' +
                ", text='" + text + '\'' +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", userIdsWhoLikedThisPost=" + userIdsWhoLikedThisPost +
                ", commentIdsWhoCommentedThisPost=" + commentIdsWhoCommentedThisPost +
                '}';
    }
}
