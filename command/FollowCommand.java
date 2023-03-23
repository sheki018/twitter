package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.List;

public class FollowCommand extends Command{

    public FollowCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "follow";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserInputScanner input = new UserInputScanner();
        String userName = input.getInput("Who do you want to follow? @");
        if(userName.equals("")){
            return;
        }
        if(userRepository.noUserName(userName)||userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            printer.printError("There is no account named " + userName + " .");
            return;
        }
        if(userName.equals(userRepository.getUserName(user))){
            printer.printError("You cannot follow yourself.");
            return;
        }
        User userToFollow = userRepository.getUser(userName);
        List<User> following = user.getFollowingUsers();
        if(following.contains(userToFollow)){
            printer.printError("You are already following @" + userName);
            return;
        }
        user.addFollowing(userToFollow);
        userToFollow.addFollowers(user);
        userToFollow.addNotifications("@"+userRepository.getUserName(user)+" follows you");
        printer.printSuccess("You are now following " + userName + ".");
    }
}
