package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//tweet, message, comment, notification can have a base class
public class Tweet {
    private String userName;
    private String tweet;
    private final List<String> likes = new ArrayList<>();
    private Date tweetDate;
    private List<Comment> comments = new ArrayList<>();
    private final List<String> retweets = new ArrayList<>();
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
    public void removeComment(Comment comment){
        comments.remove(comment);
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

    public void retweet(String userName) {
        retweets.add(userName);
    }
    public void undoRetweet(String userName){retweets.remove(userName);}

    public int getRetweetsCount(){
        return retweets.size();
    }

    public List<String> retweetedBy(){
        return retweets;
    }
    public void commentTweet(String userName, String comment) {
        comments.add(new Comment(userName, comment));
    }

    public int getCommentsCount(){
        return comments.size();
    }
}
