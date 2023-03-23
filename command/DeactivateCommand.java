package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.Date;

public class DeactivateCommand extends Command{

    public DeactivateCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "deactivate";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserInputScanner input = new UserInputScanner();
        String reply = input.getInput("Are you sure you want to deactivate your account?(yes/no) ");
        if(reply.equalsIgnoreCase("yes")){
            userRepository.deactivateUser(user, new Date());
            userRepository.signoutUser(user);
            printer.printContent("If you don't signin to your account in another 10 min your account will be permanently deleted.");
            printer.printSuccess("Signing you out...");
        }
    }
}
