package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.List;

public class FollowCommand implements Command{
    private static final String code = "follow";
    private final UserRepository userRepository;
    private final Printer printer;

    public FollowCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
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

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
