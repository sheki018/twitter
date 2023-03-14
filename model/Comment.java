package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {
    private String userName;
    private String comment;
    private Date commentDate;
    public Comment(String userName, String comment){
        this.userName = userName;
        this.comment = comment;
        this.commentDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}
