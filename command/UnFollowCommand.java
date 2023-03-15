package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class UnFollowCommand implements Command {
    private static final String code = "unfollow";
    private final UserRepository userRepository;
    private final Printer printer;

    public UnFollowCommand(UserRepository userRepository, Printer printer){
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
        List<User> followingUsers = user.getFollowingUsers();
        if(followingUsers.size()==0){
            printer.printContent("You are not following anyone.");
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
        GetInput input = new GetInput();
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
            printer.printContent("You are not following " + userName + " .");
            return;
        } else if (flag==2) {
            printer.printContent("User unavailable.");
        } else{
            user.removeFollowing(userToUnFollow);
            userToUnFollow.removeFollowers(user);
        }
        printer.printContent("You unfollowed @" + userName + ".");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
