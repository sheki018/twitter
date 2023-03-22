package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    protected String userName;
    protected List<Tweet> tweets = new ArrayList<>();
    private List<User> followingUsers = new ArrayList<>();
    private List<User> followers = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();
    private List<Tweet> retweets = new ArrayList<>();

    private Profile profile;
    //
    public User(String userName){
        this.userName = userName;
        this.profile=new Profile(userName);
    }

    public void addTweet(String tweet){
        tweets.add(new Tweet(userName, tweet));
    }
    public void deleteTweet(Tweet tweet){
        tweets.remove(tweet);
    }
    public void retweet(Tweet tweet){
        new Retweet(tweet);
        retweets.add(tweet);
    }
    public List<Tweet> getRetweets(){return retweets;}
    public void setRetweets(List<Tweet> tweets){
        this.retweets=tweets;
    }
    public void addFollowing(User user){
        if(!followingUsers.contains(user)){
            followingUsers.add(user);
        }
    }
    public void removeFollowing(User user){
        followingUsers.remove(user);
    }
    public int followingCount(){
        return followingUsers.size();
    }
    public void addFollowers(User user){
        if(!followers.contains(user)){
            followers.add(user);
        }
    }
    public void removeFollowers(User user){followers.remove(user);}
    public int followersCount(){return followers.size();}
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public List<Tweet> getTweets(){
        return tweets;
    }
    public void setTweets(List<Tweet> tweets){
        this.tweets = tweets;
    }
    public List<User> getFollowingUsers(){
        return followingUsers;
    }
    public void setFollowingUsers(List<User> followingUsers){
        this.followingUsers = followingUsers;
    }
    public List<User> getFollowers() {
        return followers;
    }
    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
    public Profile getProfile() {
        return profile;
    }
    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    public void addMessage(String userName, String message){
        messages.add(new Message(userName, message));
    }
    public List<Message> getMessages(){
        return messages;
    }
    public void addNotifications(String notification){
        notifications.add(new Notification(userName, notification));
    }
    public List<Notification> viewNotifications(){
        return notifications;
    }
    @Override
    public int hashCode(){
        int result = 1;
        final int prime = 31;
        result = prime * result + ((userName==null) ? 0 : userName.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        User other = (User) obj;
        return userName.equals(other.userName);
    }
}