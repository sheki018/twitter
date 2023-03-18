package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;
import validation.UserValidator;

import java.util.ArrayList;
import java.util.List;

public class CommentCommand implements Command{
    private static final String code = "comment";
    private final UserRepository userRepository;
    private final Printer printer;
    private final UserValidator validator;

    public CommentCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
        this.validator=new UserValidator(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printContent("Signin first. Use the command 'signin'.");
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
            printer.printContent("No tweets to display.");
            return;
        }
        tweets.sort((t1,t2) -> t2.getTweetDate().compareTo(t1.getTweetDate()));
        int index = 1;
        for (Tweet tweet:
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet);
            index++;
        }
        GetInput input = new GetInput();
        int tweetIndex=Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to comment on? Provide the index of the tweet that is displayed. "));

            } catch (NumberFormatException e) {
                printer.printContent("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size()||tweetIndex<1) {
                printer.printContent("Enter a valid number.");
            }
        }while (tweetIndex > tweets.size()||tweetIndex<1);
        String comment;
        do {
            comment = input.getInput("Your comment: ");
            if (comment.isEmpty()||validator.isBlank(comment)) {
                printer.printContent("Comment cannot be empty.");
            }
        }while (comment.isEmpty()||validator.isBlank(comment));
        tweets.get(tweetIndex-1).commentTweet(userRepository.getUserName(user), comment);
        String userName = tweets.get(tweetIndex-1).getUserName();
        if(!userName.equalsIgnoreCase(userRepository.getUserName(user))){
            userRepository.getUser(userName).addNotifications("@"+userRepository.getUserName(user)+" commented on your tweet");
        }
        printer.printContent("Comment saved.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
