package ui.output;

import model.Comment;
import model.Message;
import model.Notification;
import model.Tweet;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Printer {
    private static final String DISPLAY_FORMAT_FOR_TWEET = "@%s - %s\n%s";
    //@sheki - 22min
    //tweet
    private static final String DISPLAY_FORMAT_FOR_FEED = "@%s - %s\n%s\n%s comments    %s reposts   %s likes";
    //@sheki - 22min
    //tweet
    //commentcount      repostcount     likecount
    //liked by -
    //reposted by -
    //comments -
    private static final String DISPLAY_FORMAT_FOR_CHAT = "@%s: %s   - %s\n";
    //@sheki: message - 20min
    //@alice: message - 10min
    private static final String DISPLAY_FORMAT_FOR_NOTIFICATION = "%s    - %s\n\n";
    //@username     - 5 min ago
    //notification - "New message from @sender." || "@user follows you." || "@user liked your tweet" || "@user commented on your tweet" || @username tweeted
    private final PrintStream printStream;

    public Printer(PrintStream printStream) {
        this.printStream=printStream;
    }

    public void printContent(String content){
        printStream.println("\u001B[36m" + content + "\u001B[0m");
    }
    public void printError(String error){
        printStream.println("\u001B[31m" + error + "\u001B[0m");
    }
    public void printSuccess(String success){
        printStream.println("\u001B[32m" + "\u001B[1m" + success + "\u001B[21m" + "\u001B[0m");
    }
    public void printContentWithNoStyling(String content){
        printStream.println(content);
    }
    public void printMessage(Message message) {
        printStream.printf((DISPLAY_FORMAT_FOR_CHAT), "\u001B[1m" + message.getUserName() + "\u001B[21m" + "\u001B[0m", message.getMessage(), "\u001B[4m" + message.getDate() + "\u001B[24m" + "\u001B[0m");
    }
    public void printFeed(Tweet tweet, String type){
        printStream.printf((DISPLAY_FORMAT_FOR_FEED)+"\n", type.equals("verified") ? "\u001B[34m" + tweet.getUserName() + "\u001B[0m" : "\u001B[93m" + tweet.getUserName() + "\u001B[0m", "\u001B[4m" + getTimeAgo(tweet.getDate().getTime()) + "\u001B[24m", tweet.getTweet(), tweet.getCommentsCount(), tweet.getRetweetsCount(), tweet.getLikesCount());
        if(tweet.getLikesCount()!=0){
            printStream.println("\u001B[1mLiked by: " + "\u001B[21m" + "\u001B[0m" + tweet.likedBy().toString());
        }
        if(tweet.getRetweetsCount()!=0){
            printStream.println("\u001B[1mRetweeted by: " + "\u001B[21m" + "\u001B[0m" + tweet.retweetedBy().toString());
        }
        if(tweet.getCommentsCount()!=0){
            printStream.print("\u001B[1mComments:" + "\u001B[21m" + "\u001B[0m" + "\n");
            printComment(tweet, type);
        }
        printStream.println();
    }
    public void printComment(Tweet tweet, String type){
        List<Comment> comments = tweet.getComments();
        comments.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        for(Comment comment : comments) {
            printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n", type.equals("verified") ? "\u001B[34m" + comment.getUserName() + "\u001B[0m" : "\u001B[93m" + comment.getUserName() + "\u001B[0m", "\u001B[4m" + getTimeAgo(comment.getDate().getTime()) + "\u001B[24m", comment.getComment());
        }
    }
    public void printComment(Comment comment, String type){
        printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n", type.equals("verified") ? "\u001B[34m" + comment.getUserName() + "\u001B[0m" : "\u001B[93m" + comment.getUserName() + "\u001B[0m", "\u001B[4m" + getTimeAgo(comment.getDate().getTime()) + "\u001B[24m", comment.getComment());
    }

    public void printTweet(Tweet tweet, String type){
        printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n\n", type.equals("verified") ? "\u001B[34m" + tweet.getUserName() + "\u001B[0m" : "\u001B[93m" + tweet.getUserName() + "\u001B[0m", "\u001B[4m" + getTimeAgo(tweet.getDate().getTime()) + "\u001B[24m", tweet.getTweet());
    }

    public void printNotification(Notification notification) {
        printStream.printf((DISPLAY_FORMAT_FOR_NOTIFICATION), "\u001B[93m" + notification.getNotification() + "\u001B[0m", "\u001B[4m" + getTimeAgo(notification.getDate().getTime()) + "\u001B[24m");
    }

    private static String getTimeAgo(long tweetDate) {
        long now = System.currentTimeMillis();
        long diff = now - tweetDate;
        long timeUnitDiff;
        String timeUnit;

        if(diff< TimeUnit.MINUTES.toMillis(1)){
            timeUnitDiff = TimeUnit.MILLISECONDS.toSeconds(diff);
            timeUnit = "second";
        }else if(diff< TimeUnit.HOURS.toMillis(1)){
            timeUnitDiff = TimeUnit.MILLISECONDS.toMinutes(diff);
            timeUnit = "minute";
        }else if(diff< TimeUnit.DAYS.toMillis(1)){
            timeUnitDiff = TimeUnit.MILLISECONDS.toHours(diff);
            timeUnit = "hour";
        }else {
            timeUnitDiff = TimeUnit.MILLISECONDS.toDays(diff);
            timeUnit = "day";
        }

        if(timeUnitDiff > 1){
            timeUnit = timeUnit + "s";
        }

        return String.format("(%d %s ago)", timeUnitDiff, timeUnit);
    }
}
