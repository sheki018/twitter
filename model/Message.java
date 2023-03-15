package model;

import java.util.Date;

public class Message {
    private String userName;
    private String message;
    private Date messageDate;
    public Message(String userName, String message){
        this.userName = userName;
        this.message = message;
        this.messageDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String tweet) {
        this.message = tweet;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date tweetDate) {
        this.messageDate = tweetDate;
    }
}
