package command;

import model.User;
import repository.UserRepository;
import ui.output.Printer;


public class SignOutCommand extends BaseCommand implements Command{
    private static final String code = "signout";

    public SignOutCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
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

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
