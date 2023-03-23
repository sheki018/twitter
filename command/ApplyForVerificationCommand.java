package command;

import model.User;
import repository.UserRepository;
import ui.output.Printer;

public class ApplyForVerificationCommand extends BaseCommand implements Command{
    private static final String code = "apply";
    public ApplyForVerificationCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        if(userRepository.getAccountType(user).equals("verified")){
            printer.printContent("You are already verified!");
            return;
        }
        if(user.getTweets().size()<5){
            printer.printContent("You are not eligible.");
            return;
        }
        if(user.getProfile().getBio()==null||user.getProfile().getLocation()==null){
            printer.printContent("You are not eligible.");
            return;
        }
        userRepository.verifyUser(user);
        user.addNotifications("You are now a verified user!");
        printer.printContent("Hurray! You are a verified user now!");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
