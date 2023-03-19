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
    //Liked by: %s
    //Reposted by: %s
    //Comments: %s

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
        printStream.println(content);
    }

    public void printMessage(Message message) {
        printStream.printf((DISPLAY_FORMAT_FOR_CHAT), message.getUserName(), message.getMessage(), message.getMessageDate());
    }
    public void printFeed(Tweet tweet){
        printStream.printf((DISPLAY_FORMAT_FOR_FEED)+"\n", tweet.getUserName(), getTimeAgo(tweet.getTweetDate().getTime()), tweet.getTweet(), tweet.getCommentsCount(), tweet.getRetweetsCount(), tweet.getLikesCount());
        if(tweet.getLikesCount()!=0){
            printStream.println("Liked by: " + tweet.likedBy().toString());
        }
        if(tweet.getRetweetsCount()!=0){
            printStream.println("Retweeted by: " + tweet.retweetedBy().toString());
        }
        if(tweet.getCommentsCount()!=0){
            printStream.print("Comments:\n");
            printComment(tweet);
        }
        printStream.println();
    }
    public void printComment(Tweet tweet){
        List<Comment> comments = tweet.getComments();
        comments.sort((t1,t2) -> t2.getCommentDate().compareTo(t1.getCommentDate()));
        for(Comment comment : comments) {
            printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n", comment.getUserName(), getTimeAgo(comment.getCommentDate().getTime()), comment.getComment());
        }
    }
    public void printComment(Comment comment){
        printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n", comment.getUserName(), getTimeAgo(comment.getCommentDate().getTime()), comment.getComment());
    }

    public void printTweet(Tweet tweet){
        printStream.printf((DISPLAY_FORMAT_FOR_TWEET)+"\n\n", tweet.getUserName(), getTimeAgo(tweet.getTweetDate().getTime()), tweet.getTweet());
    }

    public void printNotification(Notification notification) {
        printStream.printf((DISPLAY_FORMAT_FOR_NOTIFICATION), notification.getNotification(), getTimeAgo(notification.getNotificationDate().getTime()));
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
