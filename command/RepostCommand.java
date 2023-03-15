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
    private final Printer printer;

    public RepostCommand(UserRepository userRepository, Printer printer){
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
        List<Tweet> tweets = new ArrayList<>();
        for (User following:
                user.getFollowingUsers()) {
            if(userRepository.isDeactivatedUser(following)){
                continue;
            }
            tweets.addAll(following.getTweets());
        }
        if(tweets.size()==0){
            printer.printContent("No tweets available to repost.");
            return;
        }
        tweets.sort((t1,t2) -> t2.getTweetDate().compareTo(t1.getTweetDate()));
        int index = 1;
        for (Tweet tweet:
                tweets) {
            printer.printContent(index + ". ");
            printer.printTweet(tweet);
            index++;
        }
        GetInput input = new GetInput();
        int tweetIndex=Integer.MAX_VALUE;
        do {
            try {
                tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to repost? Provide the index of the tweet that is displayed. "));
            } catch (NumberFormatException e) {
                printer.printContent("Enter a valid number.");
                continue;
            }
            if (tweetIndex > tweets.size()||tweetIndex<1) {
                printer.printContent("Enter a valid number.");
            }
        }while (tweetIndex > tweets.size()||tweetIndex<1);
        user.repost(tweets.get(tweetIndex-1).getTweet());
        for (User followers:
                user.getFollowers()) {
            followers.addNotifications("@" + userRepository.getUserName(user) + " reposted");
        }
        printer.printContent("Reposted.");
        //todo reposted tweet should have original tweet's username and mention the other user reposted.... check twitter profile page once
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
