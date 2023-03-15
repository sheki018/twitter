package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {
    private String userName;
    private String tweet;
    private List<String> likes = new ArrayList<>();
    private Date tweetDate;
    private List<Comment> comments = new ArrayList<>();
    public Tweet(String userName, String tweet){
        this.userName = userName;
        this.tweet = tweet;
        this.tweetDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getTweetDate() {
        return tweetDate;
    }

    public void setTweetDate(Date tweetDate) {
        this.tweetDate = tweetDate;
    }

    public List<Comment> getComments(){
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void likeTweet(String userName) {
        likes.add(userName);
    }
    public void unLikeTweet(String userName){likes.remove(userName);}

    public int getLikesCount(){
        return likes.size();
    }

    public List<String> likedBy(){
        return likes;
    }

    public void commentTweet(String userName, String comment) {
        comments.add(new Comment(userName, comment));
    }
    //todo remove comment feature

    public int getCommentsCount(){
        return comments.size();
    }
}
