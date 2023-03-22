package validation;

import model.User;
import repository.UserRepository;
import ui.output.Printer;

public class TweetValidator extends Validator{
    public TweetValidator(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
    }

    public boolean validateTweet(String tweet){
        if(isBlank(tweet)){
            super.printer.printError("Cannot be empty");
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
}
