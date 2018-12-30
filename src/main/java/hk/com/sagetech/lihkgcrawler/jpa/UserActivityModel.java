package hk.com.sagetech.lihkgcrawler.jpa;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="USER_ACTIVITY")
@IdClass(UserActivityCompositeKey.class)
public final class UserActivityModel {

    @Transient
    private static final int CONTENT_LENGTH_LIMIT = 4000;

    @Id
    @Column(name="THREAD_ID")
    private int threadId;

    @Column(name="THREAD_TITLE")
    private String threadTitle;

    @Column(name="THREAD_PAGE")
    private int threadPage;

    @Id
    @Column(name="COMMENT_NO")
    private int commentNumber;

    @Column(name="USER_ID")
    private int userId;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name="CONTENT")
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

    public void setContent(String content) {
        if(content==null){
            this.content="";
        }else if(content.length()>CONTENT_LENGTH_LIMIT){
            this.content = content.substring(0, CONTENT_LENGTH_LIMIT);
        }else{
            this.content = content;
        }
    }
}
