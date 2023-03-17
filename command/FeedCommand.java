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
    private final Printer tweetViewer;

    public FeedCommand(UserRepository userRepository, Printer tweetViewer){
        this.userRepository = userRepository;
        this.tweetViewer = tweetViewer;
    }
    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            tweetViewer.printContent("Signin first. Use the command 'signin'.");
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
            tweetViewer.printContent("No tweets to display.");
        }
        for (Tweet tweet:
             tweets) {
            tweetViewer.printFeed(tweet);
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
