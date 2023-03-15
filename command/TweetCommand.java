package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;
import validation.UserValidator;

public class TweetCommand implements Command{
    private static final String code = "tweet";
    private final UserRepository userRepository;
    private final Printer printer;
    private final UserValidator validator;

    public TweetCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.printer = displayMessage;
        this.validator = new UserValidator();
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String tweet = input.getInput("What's in your mind? ");
        if(tweet.isEmpty()||validator.isBlank(tweet)){
            printer.printContent("Tweet cannot be empty.");
        }
        if(tweet.length()>280){
            printer.printContent("Maximum characters allowed: 280.");
        }

        user.addTweet(tweet);
        for (User followers:
                user.getFollowers()) {
            followers.addNotifications("@" + userRepository.getUserName(user) + " tweeted");
        }
        printer.printContent("Your tweet is successfully posted.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
