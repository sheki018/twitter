package command;

import model.User;
import repository.UserRepository;
import ui.input.UserRegister;
import ui.output.Printer;

public class SignUpCommand implements Command{
    private static final String code = "signup";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public SignUpCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user != null){
            displayMessage.printContent("A user is already signed in. Sign out to proceed. Use 'signout' command.");
            return;
        }
        UserRegister userRegister = new UserRegister();
        String[] details = userRegister.getDetails();
        userRepository.registerUser(details);
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}