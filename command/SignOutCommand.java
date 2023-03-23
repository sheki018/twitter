package command;

import model.User;
import repository.UserRepository;
import ui.output.Printer;


public class SignOutCommand extends Command {

    public SignOutCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "signout";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        userRepository.signoutUser(user);
        printer.printSuccess("See you later.");
    }
}
