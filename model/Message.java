package model;

public class Message extends UserAction{
    private String message;

    public Message(String userName, String message) {
        super(userName);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String tweet) {
        this.message = tweet;
    }
}
