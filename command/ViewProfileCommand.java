package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.*;

public class ViewProfileCommand implements Command{
    private static final String code = "profile";
    private final UserRepository userRepository;
    private final Printer printer;

    public ViewProfileCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        User activeUser = userRepository.getActiveUser();
        if(activeUser == null){
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String userName = input.getInput("Provide the username of the user's profile you want to see: ");
        if(userRepository.noUserName(userName)|| userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            printer.printContent("There is no account named " + userName + " .");
            return;
        }
        User user = userRepository.getUser(userName);
        String name = userRepository.getName(user);
        printer.printContent(name);
        printer.printContent("@"+userName);
        if(user.getProfile().getBio()!=null){
            printer.printContent("Bio: " + user.getProfile().getBio());
        }
        if(user.getProfile().getLocation()!=null){
            printer.printContent("Location: " + user.getProfile().getLocation());
        }
        printer.printContent(user.followingCount()+" following\t\t"+user.followersCount()+" followers");
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
        Comparator<Tweet> byTweet = (Tweet t1, Tweet t2) -> t2.getTweetDate().compareTo(t1.getTweetDate());
        Map<Tweet, Map<String, String>> sortedTweetsMap = new TreeMap<>(byTweet);
        sortedTweetsMap.putAll(tweetsMap);
        if(tweetsMap.size()==0){
            printer.printContent("No tweets to display.");
            return;
        }
        printer.printContent("------------------Tweets------------------");
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
            printer.printFeed(tweet);
        }
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
