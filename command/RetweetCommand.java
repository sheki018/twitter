package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class RetweetCommand extends Command {

    public RetweetCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "retweet";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        List<Tweet> tweets = new ArrayList<>();
        for (User following:
                user.getFollowingUsers()) {
            if(userRepository.isDeactivatedUser(following)){
                continue;
            }
            tweets.addAll(following.getTweets());
        }
        if(tweets.size()==0){
            printer.printError("No tweets available to repost.");
            return;
        }
        tweets.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        int index = 1;
        for (Tweet tweet:
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet, userRepository.getAccountType(userRepository.getUser(tweet.getUserName())));
            index++;
        }
        UserInputScanner input = new UserInputScanner();
        int tweetIndex=Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to repost? Provide the index of the tweet that is displayed. "));
            } catch (NumberFormatException e) {
                printer.printError("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size()||tweetIndex<1) {
                printer.printError("Enter a valid number.");
            }
        }while (tweetIndex > tweets.size()||tweetIndex<1);
        List<String> retweetedBy = tweets.get(tweetIndex-1).retweetedBy();
        if(retweetedBy.contains(userRepository.getUserName(user))){
            printer.printError("You have already retweeted the tweet.");
            if(input.getInput("Do you want to undo your action? ").equalsIgnoreCase("yes")){
                tweets.get(tweetIndex-1).undoRetweet(userRepository.getUserName(user));
                printer.printSuccess("Your action was recorded.");
            }
        }else{
            tweets.get(tweetIndex-1).retweet(userRepository.getUserName(user));
            user.retweet(tweets.get(tweetIndex-1));
            for (User followers:
                    user.getFollowers()) {
                followers.addNotifications("@" + userRepository.getUserName(user) + " reposted");
            }
            printer.printSuccess("Reposted.");
        }
    }
}
