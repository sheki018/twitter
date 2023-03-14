package command;

import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.List;

public class ViewProfileCommand implements Command{
    private static final String code = "profile";
    private final UserRepository userRepository;
    private final Printer displayInformation;

    public ViewProfileCommand(UserRepository userRepository, Printer tweetViewer){
        this.userRepository = userRepository;
        this.displayInformation = tweetViewer;
    }

    @Override
    public void execute(String command) {
        User activeUser = userRepository.getActiveUser();
        if(activeUser == null){
            displayInformation.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String userName = input.getInput("Provide the username of the user's profile you want to see: ");
        if(userRepository.noUserName(userName)){
            displayInformation.printContent("There is no account named " + userName + " .");
            return;
        }
        User user = userRepository.getUser(userName);
        String name = userRepository.getName(user);
        displayInformation.printContent(name);
        displayInformation.printContent("@"+userName);
        if(user.getProfile().getBio()!=null){
            displayInformation.printContent("Bio: " + user.getProfile().getBio());
        }
        if(user.getProfile().getLocation()!=null){
            displayInformation.printContent("Location: " + user.getProfile().getLocation());
        }
        displayInformation.printContent(user.followingCount()+" following\t\t"+user.followersCount()+" followers");
        List<Tweet> tweets = user.getTweets();
        if(tweets.size()==0){
            displayInformation.printContent("No tweets to display.");
        }else {
            displayInformation.printContent("------------------Tweets------------------");
            tweets.sort((t1, t2) -> t2.getTweetDate().compareTo(t1.getTweetDate()));
            for (Tweet tweet : tweets) {
                displayInformation.printTweet(tweet);
            }
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
