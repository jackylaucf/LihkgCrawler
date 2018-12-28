package hk.com.sagetech.lihkgcrawler;

import java.time.LocalDateTime;

public final class UserActivityModel {

    private int threadId;
    private String threadTitle;
    private int threadPage;
    private int commentNumber;
    private int userId;
    private String userName;
    private LocalDateTime dateTime;
    private String content;

    public UserActivityModel(){
        //Empty Constructor
    }

    public int getThreadId() { return threadId; }

    public void setThreadId(int threadId) { this.threadId = threadId; }

    public String getThreadTitle() { return threadTitle; }

    public void setThreadTitle(String threadTitle) { this.threadTitle = threadTitle; }

    public int getthreadPage() { return threadPage; }

    public void setThreadPage(int threadPage) { this.threadPage = threadPage; }

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
