package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class UnFollowCommand extends Command{

    public UnFollowCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "unfollow";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        List<User> followingUsers = user.getFollowingUsers();
        if(followingUsers.size()==0){
            printer.printError("You are not following anyone.");
            return;
        }
        List<String> followingUserNames = new ArrayList<>();
        for (User person:
             followingUsers) {
            if(userRepository.isDeactivatedUser(person)){
                continue;
            }
            followingUserNames.add(userRepository.getUserName(person));
        }
        printer.printContent("You are following: " + followingUserNames);
        UserInputScanner input = new UserInputScanner();
        String userName = input.getInput("Who do you want to unfollow? @");
        if(userName.equals("")){
            return;
        }
        User userToUnFollow = userRepository.getUser(userName);
        int flag=0;
        for (User person: followingUsers) {
            if(userRepository.isDeactivatedUser(person)){
                flag=2;
            }
            if(userToUnFollow==person){
                flag=1;
                break;
            }
        }
        if(flag==0){
            printer.printError("You are not following " + userName + " .");
            return;
        } else if (flag==2) {
            printer.printError("User unavailable.");
        } else{
            user.removeFollowing(userToUnFollow);
            userToUnFollow.removeFollowers(user);
        }
        printer.printSuccess("You unfollowed @" + userName + ".");
    }
}
