package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

public class DeactivateCommand implements Command{
    private static final String code = "deactivate";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public DeactivateCommand(UserRepository userRepository, Printer displayMessage){
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
        GetInput input = new GetInput();
        String reply = input.getInput("Are you sure you want to deactivate your account?(yes/no) ");
        if(reply.equalsIgnoreCase("yes")){
            userRepository.deactivateUser(user);
            userRepository.signoutUser(user);
            displayMessage.printContent("If you don't signin to your account in another 10 min your account will be permanently deleted.");
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
