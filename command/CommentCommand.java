package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;
import validation.TweetValidator;

import java.util.ArrayList;
import java.util.List;

public class CommentCommand extends Command{
    private final TweetValidator validator;

    public CommentCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.validator=new TweetValidator(userRepository, printer);
        this.code = "comment";
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
        if(tweets.size()==0){
            printer.printError("No tweets to display.");
            return;
        }
        tweets.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        int index = 1;
        String type = userRepository.getAccountType(user);
        for (Tweet tweet:
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet, type);
            index++;
        }
        UserInputScanner input = new UserInputScanner();
        int tweetIndex=Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to comment on? Provide the index of the tweet that is displayed. "));

            } catch (NumberFormatException e) {
                printer.printError("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size()||tweetIndex<1) {
                printer.printError("Enter a valid number.");
            }
        }while (tweetIndex > tweets.size()||tweetIndex<1);
        String comment;
        do {
            comment = input.getInput("Your comment: ");
        }while (validator.validateTweet(comment));
        tweets.get(tweetIndex-1).commentTweet(userRepository.getUserName(user), comment);
        String userName = tweets.get(tweetIndex-1).getUserName();
        if(!userName.equalsIgnoreCase(userRepository.getUserName(user))){
            userRepository.getUser(userName).addNotifications("@"+userRepository.getUserName(user)+" commented on your tweet");
        }
        printer.printSuccess("Comment saved.");
    }
}
