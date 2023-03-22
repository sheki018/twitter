package validation;

import model.User;
import repository.UserRepository;
import ui.output.Printer;

public class TweetValidator {
    private final UserRepository userRepository;
    private final Printer printer;

    public TweetValidator(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }

    public boolean validateTweet(String tweet){
        if(isBlank(tweet)){
            printer.printError("Cannot be empty");
            return true;
        }
        return false;
    }
    public boolean validateTweetLength(String tweet, User user){
        if(userRepository.getAccountType(user).equals("normal")){
            if(tweet.length()>280){
                printer.printError("Maximum characters allowed: 280.");
                return false;
            }
        }else{
            if(tweet.length()>4000){
                printer.printError("Maximum characters allowed: 4000.");
                return false;
            }
        }
        return true;
    }
    private boolean isBlank(String input){
        return input.trim().isEmpty();
    }
}
