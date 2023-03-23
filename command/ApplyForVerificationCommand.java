package command;

import model.User;
import repository.UserRepository;
import ui.output.Printer;

public class ApplyForVerificationCommand implements Command{
    private static final String code = "apply";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public ApplyForVerificationCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.displayMessage = displayMessage;
    }
    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            displayMessage.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        if(userRepository.getAccountType(user).equals("verified")){
            displayMessage.printContent("You are already verified!");
            return;
        }
        if(user.getTweets().size()<5){
            displayMessage.printContent("You are not eligible.");
            return;
        }
        if(user.getProfile().getBio()==null||user.getProfile().getLocation()==null){
            displayMessage.printContent("You are not eligible.");
            return;
        }
        userRepository.verifyUser(user);
        user.addNotifications("You are now a verified user!");
        displayMessage.printContent("Hurray! You are a verified user now!");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
