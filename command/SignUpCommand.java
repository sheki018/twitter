package command;

import model.User;
import repository.UserRepository;
import ui.input.UserRegister;
import ui.output.Printer;

public class SignUpCommand extends Command {
    public SignUpCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "signup";
    }

    @Override
    public void execute(String command) {
        // user repository register user can be accessible only from signup command - so that multiple instances of user repository cannot be created
        User user = userRepository.getActiveUser();
        if(user != null){
            printer.printError("A user is already signed in. Sign out to proceed. Use 'signout' command.");
            return;
        }
        UserRegister userRegister = new UserRegister(userRepository, printer);
        String[] details = userRegister.userDetails();
        userRepository.registerUser(details);
        printer.printSuccess("Account has been successfully created! Sign in to continue. Use the command 'signin'.");
    }
}