package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

public class UpdateProfileCommand implements Command{
    private static final String code = "update";
    private final UserRepository userRepository;
    private final Printer printer;

    public UpdateProfileCommand(UserRepository userRepository, Printer printer){
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
        String userName = userRepository.getUserName(user);
        GetInput input = new GetInput();
        String changeItem = input.getInput("What do you want to update (bio, location, password)? ");
        switch (changeItem) {
            case "bio":
                user.getProfile().setBio(input.getInput("Bio: "));
                printer.printContent("Updated bio successfully!");
                break;
            case "location":
                user.getProfile().setLocation(input.getInput("Location: "));
                printer.printContent("Updated location successfully!");
                break;
            case "password":
                String oldPassword;
                do{
                    oldPassword = input.getInput("Old password: ");
                    if(userRepository.notMatchesPassword(userName, oldPassword)){
                        printer.printContent("Incorrect password");
                    }
                }while (userRepository.notMatchesPassword(userName, oldPassword));
                String newPassword;
                do {
                    newPassword = input.getInput("New password: ");
                    if (!userRepository.notMatchesPassword(userName, newPassword)) {
                        printer.printContent("The new password cannot be same as the old password.");
                    }
                }while (!userRepository.notMatchesPassword(userName, newPassword));
                String confirmPassword = input.getInput("Confirm password: ");
                // todo validate equality of both new and confirm password and new password shouldnt be equal to old password
                userRepository.updatePassword(userName, newPassword);
                printer.printContent("Updated password successfully!");

                /*
                 automatically signs out user after changing password
                userRepository.signoutUser(user);
                printer.printContent("You are signed out.");
                */

                break;
            default:
                InvalidCommand invalidCommand = new InvalidCommand(printer);
                invalidCommand.execute(changeItem);
                break;
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
