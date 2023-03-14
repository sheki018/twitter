package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

public class UpdateProfileCommand implements Command{
    private static final String code = "update";
    private final UserRepository userRepository;
    private final Printer displayInformation;

    public UpdateProfileCommand(UserRepository userRepository, Printer tweetViewer){
        this.userRepository = userRepository;
        this.displayInformation = tweetViewer;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            displayInformation.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        String userName = userRepository.getUserName(user);
        GetInput input = new GetInput();
        String changeItem = input.getInput("What do you want to update (bio, location, password)? ");
        switch (changeItem) {
            case "bio":
                user.getProfile().setBio(input.getInput("Bio: "));
                displayInformation.printContent("Updated bio successfully!");
                break;
            case "location":
                user.getProfile().setLocation(input.getInput("Location: "));
                displayInformation.printContent("Updated location successfully!");
                break;
            case "password":
                String oldPassword;
                do{
                    oldPassword = input.getInput("Old password: ");
                    if(userRepository.notMatchesPassword(userName, oldPassword)){
                        displayInformation.printContent("Incorrect password");
                    }
                }while (userRepository.notMatchesPassword(userName, oldPassword));
                String newPassword = input.getInput("New password: ");
                String confirmPassword = input.getInput("Confirm password: ");
                // todo validate equality of both new and confirm password and new password shouldnt be equal to old password
                userRepository.updatePassword(userName, newPassword);
                displayInformation.printContent("Updated password successfully!");
                // todo should we automatically signout user after changing password????
                break;
            default:
                InvalidCommand invalidCommand = new InvalidCommand(displayInformation);
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
