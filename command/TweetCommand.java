package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetCommand implements Command{
    private static final String code = "tweet";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public TweetCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            displayMessage.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String tweet = input.getInput("What's in your mind? ");

        // todo tweet length check

        user.addTweet(tweet);
        for (User followers:
                user.getFollowers()) {
            followers.addNotifications("@" + userRepository.getUserName(user) + " tweeted");
        }
        displayMessage.printContent("Your tweet is successfully posted.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
