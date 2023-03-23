package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class LikeCommand extends BaseCommand implements Command{
    private static final String code = "like";

    public LikeCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        List<Tweet> tweets = new ArrayList<>(user.getTweets());
        for (User following:
                user.getFollowingUsers()) {
            if(userRepository.isDeactivatedUser(following)){
                continue;
            }
            tweets.addAll(following.getTweets());
        }
        tweets.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        if(tweets.size()==0){
            printer.printError("No tweets to display.");
            return;
        }
        int index = 1;
        for (Tweet tweet:
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet, userRepository.getAccountType(user));
            index++;
        }
        UserInputScanner input = new UserInputScanner();
        int tweetIndex=Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to like? Provide the index of the tweet that is displayed. "));
            } catch (NumberFormatException e) {
                printer.printError("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size()||tweetIndex<1) {
                printer.printError("Enter a valid number.");
            }
        }while (tweetIndex > tweets.size()||tweetIndex<1);
        List<String> likedBy = tweets.get(tweetIndex-1).likedBy();
        if(likedBy.contains(userRepository.getUserName(user))){
            printer.printError("You have already liked the tweet.");
            if(input.getInput("Do you want to unlike the tweet? ").equalsIgnoreCase("yes")){
                tweets.get(tweetIndex-1).unLikeTweet(userRepository.getUserName(user));
                printer.printSuccess("You have unliked the tweet successfully.");
            }
        }else{
            tweets.get(tweetIndex-1).likeTweet(userRepository.getUserName(user));
            String userName = tweets.get(tweetIndex-1).getUserName();
            if(!userName.equalsIgnoreCase(userRepository.getUserName(user))){
                userRepository.getUser(userName).addNotifications("@" + userRepository.getUserName(user) + " liked your tweet.");
            }
            printer.printSuccess("Reaction saved.");
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
