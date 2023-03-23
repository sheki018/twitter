package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.*;

public class ViewProfileCommand extends Command {

    public ViewProfileCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "profile";
    }

    @Override
    public void execute(String command) {
        User activeUser = userRepository.getActiveUser();
        if(activeUser == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserInputScanner input = new UserInputScanner();
        String userName = input.getInput("Provide the username of the user's profile you want to see: ");
        if(userRepository.noUserName(userName)|| userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            printer.printError("There is no account named " + userName + " .");
            return;
        }
        User user = userRepository.getUser(userName);
        String name = userRepository.getName(user);
        if(userRepository.getAccountType(user).equals("verified")){
            printer.printContentWithNoStyling("\u001B[34m" + name + "\u001B[0m");
            printer.printContentWithNoStyling("\u001B[34m" + "@" + userName + "\u001B[0m");
        }else {
            printer.printContentWithNoStyling("\u001B[93m" + name + "\u001B[0m");
            printer.printContentWithNoStyling("\u001B[93m" + "@" + userName + "\u001B[0m");
        }
        if(user.getProfile().getBio()!=null){
            printer.printContentWithNoStyling("\u001B[93mBio: " + user.getProfile().getBio()+"\u001B[24m");
        }
        if(user.getProfile().getLocation()!=null){
            printer.printContentWithNoStyling("\u001B[93mLocation: " + user.getProfile().getLocation()+"\u001B[24m");
        }
        printer.printContentWithNoStyling("\u001B[93m" + user.followingCount()+" following\t\t"+user.followersCount()+" followers"+"\u001B[24m");
        Map<Tweet, Map<String, String>> tweetsMap = new HashMap<>();
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
        Comparator<Tweet> byTweet = (Tweet t1, Tweet t2) -> t2.getDate().compareTo(t1.getDate());
        Map<Tweet, Map<String, String>> sortedTweetsMap = new TreeMap<>(byTweet);
        sortedTweetsMap.putAll(tweetsMap);
        if(tweetsMap.size()==0){
            printer.printError("No tweets to display.");
            return;
        }
        printer.printContentWithNoStyling("\u001B[93m------------------Tweets------------------\u001B[0m");
        String type="normal";
        for (Map.Entry<Tweet, Map<String, String>> tweetEntry : sortedTweetsMap.entrySet()) {
            Tweet tweet = tweetEntry.getKey();
            Map<String, String> tweetDetails = tweetEntry.getValue();
            for (Map.Entry<String, String> innerEntry : tweetDetails.entrySet()) {
                userName = innerEntry.getKey();
                type = userRepository.getAccountType(userRepository.getUser(tweet.getUserName()));
                String tweetType = innerEntry.getValue();
                if(tweetType.equalsIgnoreCase("retweet")){
                    printer.printContent("@" + userName + " retweeted");
                }
            }
            printer.printFeed(tweet,type);
        }
    }
}
