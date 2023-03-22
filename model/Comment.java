package model;


public class Comment extends UserAction{
    private String comment;

    public Comment(String userName, String comment) {
        super(userName);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
