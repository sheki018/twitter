package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class RepostCommand implements Command{
    private static final String code = "repost";
    private final UserRepository userRepository;
    private final Printer tweetViewer;

    public RepostCommand(UserRepository userRepository, Printer tweetViewer){
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
        List<Tweet> tweets = new ArrayList<>();
        for (User following:
                user.getFollowingUsers()) {
            tweets.addAll(following.getTweets());
        }
        if(tweets.size()==0){
            tweetViewer.printContent("No tweets available to repost.");
            return;
        }
        tweets.sort((t1,t2) -> t2.getTweetDate().compareTo(t1.getTweetDate()));
        int index = 1;
        for (Tweet tweet:
                tweets) {
            tweetViewer.printContent(index + ". ");
            tweetViewer.printTweet(tweet);
            index++;
        }
        GetInput input = new GetInput();
        int tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to repost? Provide the index of the tweet that is displayed. "));
        // todo check for a valid tweet index
        user.repost(tweets.get(tweetIndex).getTweet());
        for (User followers:
                user.getFollowers()) {
            followers.addNotifications("@" + userRepository.getUserName(user) + " reposted");
        }
        tweetViewer.printContent("Reposted.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
