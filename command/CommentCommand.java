package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class CommentCommand implements Command{
    private static final String code = "comment";
    private final UserRepository userRepository;
    private final Printer tweetViewer;

    public CommentCommand(UserRepository userRepository, Printer tweetViewer){
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
        for (User following:
                user.getFollowingUsers()) {
            tweets.addAll(following.getTweets());
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
        int tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to comment on? Provide the index of the tweet that is displayed. "));
        // todo check for a valid tweet index and comment should not be empty
        String comment = input.getInput("Your comment: ");
        tweets.get(tweetIndex-1).commentTweet(userRepository.getUserName(user), comment);
        String userName = tweets.get(tweetIndex-1).getUserName();
        userRepository.getUser(userName).addNotifications("@"+userName+" commented on your tweet");
        tweetViewer.printContent("Comment saved.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
