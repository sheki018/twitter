package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.Date;

public class SignInCommand implements Command{
    private static final String code = "signin";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public SignInCommand(UserRepository userRepository, Printer displayMessage){
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
        GetInput input = new GetInput();
        String userName = input.getInput("User Name: ");
        String password;
        if(userRepository.isDeactivatedUser(userRepository.getUser(userName))&&new Date().getTime()/60000-userRepository.deactivatedTime(userRepository.getUser(userName)).getTime()/60000<=10){
            String option = input.getInput("Your account has been deactivated. Do you want to reactivate it? (yes/no) ");
            if(option.equalsIgnoreCase("no")){
                return;
            }
        }
        if(userRepository.noUserName(userName)){
            displayMessage.printContent("There is no account named " + userName + ". Do you want to create a new account? Use the command 'signup'.");
            return;
        }
        //limit incorrect password attempts to 3
        int count = 0;
        do{
            password = input.getInput("Password: ");
            if(userRepository.notMatchesPassword(userName, password)){
                displayMessage.printContent("Incorrect password");
                count++;
            }
            if(count==3){
                displayMessage.printContent("Try after some time!");
                //todo dont allow the user to signin for a specific amount of time. display a message so.
                return;
            }
        }while (userRepository.notMatchesPassword(userName, password));
        userRepository.updateStatus(userName);
        displayMessage.printContent("Login successful.");
        //remove from deactivated list
        if(userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            userRepository.activateUser(userRepository.getUser(userName));
        }
        // todo display a menu of all the commands that are available after login
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
