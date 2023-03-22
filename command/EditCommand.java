package command;

import model.Tweet;
import model.User;
import model.VerifiedUser;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;
import validation.TweetValidator;

import java.util.ArrayList;
import java.util.List;

public class EditCommand implements Command{
    private static final String code = "edit";
    private final UserRepository userRepository;
    private final Printer printer;
    private final TweetValidator tweetValidator;

    public EditCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
        this.tweetValidator = new TweetValidator(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User activeUser = userRepository.getActiveUser();
        if(activeUser == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        if(!userRepository.getAccountType(activeUser).equals("verified")){
            printer.printError("Only verified users can edit.");
            return;
        }
        VerifiedUser verifiedUser = new VerifiedUser(userRepository.getUserName( activeUser));
        List<Tweet> tweets = new ArrayList<>(activeUser.getTweets());
        tweets.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        int index = 1;
        for (Tweet tweet :
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet, userRepository.getAccountType(activeUser));
            index++;
        }
        UserInputScanner input = new UserInputScanner();
        int tweetIndex = Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to edit? Provide the index of the tweet that is displayed. "));
            } catch (NumberFormatException e) {
                printer.printError("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size() || tweetIndex < 1) {
                printer.printError("Enter a valid number.");
            }
        } while (tweetIndex > tweets.size() || tweetIndex < 1);
        String content;
        do {
            content = input.getInput("What is your new tweet? ");
        } while (tweetValidator.validateTweet(content));
        verifiedUser.editTweet(tweets.get(tweetIndex - 1), content);
        printer.printSuccess("Tweet edited.");

    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
