package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;
import validation.TweetValidator;

public class TweetCommand implements Command{
    private static final String code = "tweet";
    private final UserRepository userRepository;
    private final Printer printer;
    private final TweetValidator validator;

    public TweetCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.printer = displayMessage;
        this.validator = new TweetValidator(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String tweet;
        do {
            tweet = input.getInput("What's in your mind? ");
        }while ((!validator.validateTweetLength(tweet, user)) || (validator.validateTweet(tweet)));
        user.addTweet(tweet);
        for (User followers:
                user.getFollowers()) {
            followers.addNotifications("@" + userRepository.getUserName(user) + " tweeted");
        }
        printer.printSuccess("Your tweet is successfully posted.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
