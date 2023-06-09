package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.output.Printer;

import java.util.*;

public class FeedCommand extends Command{

    public FeedCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "feed";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        Map<Tweet, Map<String, String>> tweetsMap = new HashMap<>();
        String userName = userRepository.getUserName(user);
        Map<String, String> details = new HashMap<>();
        details.put(userName, "tweet");
        for(Tweet tweet : user.getTweets()){
             tweetsMap.put(tweet, details);
        }
        Map<String, String> retweetDetails = new HashMap<>();
        retweetDetails.put(userName, "retweet");
        for (Tweet retweet : user.getRetweets()){
            tweetsMap.put(retweet, retweetDetails);
        }
        for (User following:
             user.getFollowingUsers()) {
            if(userRepository.isDeactivatedUser(following)){
                continue;
            }
            userName = userRepository.getUserName(following);
            Map<String, String> tDetails = new HashMap<>();
            tDetails.put(userName, "tweet");
            for(Tweet tweet : following.getTweets()){
                tweetsMap.put(tweet, tDetails);
            }
            Map<String, String> rDetails = new HashMap<>();
            rDetails.put(userName, "retweet");
            for (Tweet retweet : following.getRetweets()){
                tweetsMap.put(retweet, rDetails);
            }
        }

        Comparator<Tweet> byTweet = (Tweet t1, Tweet t2) -> t2.getDate().compareTo(t1.getDate());
        Map<Tweet, Map<String, String>> sortedTweetsMap = new TreeMap<>(byTweet);
        sortedTweetsMap.putAll(tweetsMap);
        if(tweetsMap.size()==0){
            printer.printError("No tweets to display.");
            return;
        }
        for (Map.Entry<Tweet, Map<String, String>> tweetEntry : sortedTweetsMap.entrySet()) {
            Tweet tweet = tweetEntry.getKey();
            Map<String, String> tweetDetails = tweetEntry.getValue();
            for (Map.Entry<String, String> innerEntry : tweetDetails.entrySet()) {
                userName = innerEntry.getKey();
                String type = innerEntry.getValue();
                if(type.equalsIgnoreCase("retweet")){
                    printer.printContent("@" + userName + " retweeted");
                }
            }
            String type = userRepository.getAccountType(userRepository.getUser(userName));
            printer.printFeed(tweet,type);
        }

        printer.printContent("To like a tweet use the command 'like'.");
        printer.printContent("To comment on a tweet use the command 'comment'.");
        printer.printContent("To retweet a tweet use the command 'retweet'.");
    }
}
