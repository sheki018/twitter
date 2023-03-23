package command;

import model.Comment;
import model.Tweet;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends BaseCommand implements Command{
    private static final String code = "delete";

    public DeleteCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserInputScanner input = new UserInputScanner();
        String option = input.getInput("What do you want to delete? (tweet/comment) ");
        if(option.equalsIgnoreCase("comment")){
            List<Tweet> myCommentedTweets = new ArrayList<>();
            List<Tweet> tweets = user.getTweets();
            for(Tweet tweet: tweets){
                List<Comment> comments = tweet.getComments();
                for(Comment comment: comments){
                    if(comment.getUserName().equals(userRepository.getUserName(user))){
                        myCommentedTweets.add(tweet);
                        break;
                    }
                }
            }
            if(myCommentedTweets.size()==0){
                printer.printError("You have not commented on any tweets.");
                return;
            }
            int index = 1;
            String type = userRepository.getAccountType(user);
            for(Tweet tweet: myCommentedTweets){
                printer.printContent(index + ". ");
                printer.printTweet(tweet, type);
                index++;
            }
            int tweetIndex=Integer.MAX_VALUE;
            do {
                try {
                    tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to select? Provide the index of the tweet that is displayed. "));

                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                    continue;
                }
                if (tweetIndex > myCommentedTweets.size()||tweetIndex<1) {
                    printer.printError("Enter a valid number.");
                }
            }while (tweetIndex > myCommentedTweets.size()||tweetIndex<1);
            Tweet originalTweet = myCommentedTweets.get(tweetIndex-1);
            List<Comment> myComments = originalTweet.getComments();
            index = 1;
            //String type = userRepository.getAccountType(user);
            for(Comment comment : myComments){
                printer.printContent(index + ". ");
                printer.printComment(comment, type);
                index++;
            }
            int commentIndex = Integer.MAX_VALUE;
            do {
                try {
                    commentIndex = Integer.parseInt(input.getInput("Which comment do you want to delete? Provide the index of the tweet that is displayed. "));
                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                    continue;
                }
                if (commentIndex > myComments.size()||commentIndex<1) {
                    printer.printError("Enter a valid number.");
                }
            }while (commentIndex > myComments.size()||commentIndex<1);
            Comment comment = myComments.get(commentIndex - 1);
            originalTweet.removeComment(comment);
            printer.printSuccess("Comment deleted.");
        }else if(option.equalsIgnoreCase("tweet")){
            List<Tweet> tweets = new ArrayList<>(user.getTweets());
            if(tweets.size()==0){
                printer.printError("No tweets to display.");
                return;
            }
            tweets.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
            int index = 1;
            for (Tweet tweet:
                    tweets) {
                printer.printContent(index + ". ");
                printer.printTweet(tweet, userRepository.getAccountType(user));
                index++;
            }
            int tweetIndex=Integer.MAX_VALUE;
            do {
                try {
                    tweetIndex = Integer.parseInt(input.getInput("Which tweet do you want to comment on? Provide the index of the tweet that is displayed. "));

                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                    continue;
                }
                if (tweetIndex > tweets.size()||tweetIndex<1) {
                    printer.printError("Enter a valid number.");
                }
            }while (tweetIndex > tweets.size()||tweetIndex<1);
            user.deleteTweet(tweets.get(tweetIndex-1));
            printer.printSuccess("Tweet deleted.");
        }else{
            printer.printError("Choose either tweet or comment.");
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
