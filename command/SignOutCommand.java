package command;

import model.User;
import repository.UserRepository;
import ui.output.Printer;


public class SignOutCommand implements Command{
    private static final String code = "signout";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public SignOutCommand(UserRepository userRepository, Printer displayMessage){
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
        userRepository.signoutUser(user);
        displayMessage.printContent("See you later.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
