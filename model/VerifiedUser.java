package model;

import java.util.Date;

public class VerifiedUser extends User{

    public VerifiedUser(String userName) {
        super(userName);
    }
    public void editTweet(Tweet tweet, String content){
        tweet.setTweet(content);
        tweet.setTweetDate(new Date());
    }
}
