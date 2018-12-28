package hk.com.sagetech.lihkgcrawler;

import java.time.LocalDateTime;

public final class UserActivityModel {

    private int postId;
    private String postTitle;
    private int postPage;
    private int commentNumber;
    private int userId;
    private String userName;
    private LocalDateTime dateTime;
    private String content;

    public UserActivityModel(){
        //Empty Constructor
    }

    public int getPostId() { return postId; }

    public void setPostId(int postId) { this.postId = postId; }

    public String getPostTitle() { return postTitle; }

    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public int getPostPage() { return postPage; }

    public void setPostPage(int postPage) { this.postPage = postPage; }

    public int getCommentNumber() { return commentNumber; }

    public void setCommentNumber(int commentNumber) { this.commentNumber = commentNumber; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public LocalDateTime getDateTime() { return dateTime; }

    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}
