package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements Command{
    private static final String code = "feed";
    private final UserRepository userRepository;
    private final Printer printer;

    public FeedCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }
    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        List<Tweet> tweets = new ArrayList<>(user.getTweets());
        //tweets.addAll(user.getRetweets());
        for (User following:
             user.getFollowingUsers()) {
            if(userRepository.isDeactivatedUser(following)){
                continue;
            }
            tweets.addAll(following.getTweets());
            //tweets.addAll(following.getRetweets());
        }
        tweets.sort((t1,t2) -> t2.getTweetDate().compareTo(t1.getTweetDate()));
        if(tweets.size()==0){
            printer.printContent("No tweets to display.");
            return;
        }
        for (Tweet tweet:
             tweets) {
            printer.printFeed(tweet);
        }
        printer.printContent("To like a tweet use the command 'like'.");
        printer.printContent("To comment on a tweet use the command 'comment'.");
        printer.printContent("To retweet a tweet use the command 'retweet'.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
