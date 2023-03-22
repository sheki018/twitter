package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;
import validation.UserValidator;

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
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserValidator validator = new UserValidator(userRepository, printer);
        String userName = userRepository.getUserName(user);
        UserInputScanner input = new UserInputScanner();
        String changeItem = input.getInput("What do you want to update (bio, location, password)? ");
        switch (changeItem) {
            case "bio":
                String bio;
                do{
                    bio = input.getInput("Bio: ");
                }while (validator.isBlank(bio));
                user.getProfile().setBio(bio);
                printer.printSuccess("Updated bio successfully!");
                break;
            case "location":
                String location;
                do{
                    location = input.getInput("Location: ");
                }while (validator.isBlank(location));
                user.getProfile().setLocation(location);
                printer.printSuccess("Updated location successfully!");
                break;
            case "password":
                String oldPassword;
                do{
                    oldPassword = input.getInput("Old password: ");
                    if(userRepository.notMatchesPassword(userName, oldPassword)){
                        printer.printError("Incorrect password");
                    }
                }while (userRepository.notMatchesPassword(userName, oldPassword));
                String newPassword;
                do {
                    newPassword = input.getInput("New password: ");
                    if (!userRepository.notMatchesPassword(userName, newPassword)) {
                        printer.printError("The new password cannot be same as the old password.");
                    }
                }while (!userRepository.notMatchesPassword(userName, newPassword) || validator.validatePassword(newPassword));
                String confirmPassword;
                do {
                    confirmPassword = input.getInput("Confirm Password: ");
                }while (validator.validatePassword(newPassword, confirmPassword));
                userRepository.updatePassword(userName, newPassword);
                printer.printSuccess("Updated password successfully!");

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
