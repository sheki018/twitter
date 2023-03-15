package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

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
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String userName = input.getInput("Who do you want to follow? @");
        if(userName.equals("")){
            return;
        }
        if(userRepository.noUserName(userName)||userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            printer.printContent("There is no account named " + userName + " .");
            return;
        }
        if(userName.equals(userRepository.getUserName(user))){
            printer.printContent("You cannot follow yourself.");
            return;
        }
        User userToFollow = userRepository.getUser(userName);
        user.addFollowing(userToFollow);
        userToFollow.addFollowers(user);
        userToFollow.addNotifications("@"+userRepository.getUserName(user)+" follows you");
        printer.printContent("You are now following " + userName + ".");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
