package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;
import validation.UserValidator;

import java.util.Date;

public class SignInCommand implements Command{
    private static final String code = "signin";
    private final UserRepository userRepository;
    private final Printer printer;

    public SignInCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }
    @Override
    public void execute(String command) {
        UserValidator validator = new UserValidator(userRepository, printer);
        User user = userRepository.getActiveUser();
        if(user != null){
            printer.printError("A user is already signed in. Sign out to proceed. Use 'signout' command.");
            return;
        }
        GetInput input = new GetInput();
        String userName = input.getInput("User Name: ");
        String password;
        if(validator.isBlank(userName)){
            printer.printError("User name cannot be empty.");
            return;
        }
        if(userRepository.isDeactivatedUser(userRepository.getUser(userName))&&new Date().getTime()/60000-userRepository.deactivatedTime(userRepository.getUser(userName)).getTime()/60000<=10){
            String option = input.getInput("Your account has been deactivated. Do you want to reactivate it? (yes/no) ");
            if(option.equalsIgnoreCase("no")){
                return;
            }
        }else if(userRepository.isDeactivatedUser(userRepository.getUser(userName))&&new Date().getTime()/60000-userRepository.deactivatedTime(userRepository.getUser(userName)).getTime()/60000>10){
            userRepository.deleteUser(userRepository.getUser(userName));
        }
        if(userRepository.noUserName(userName)){
            printer.printError("There is no account named " + userName + ". Do you want to create a new account? Use the command 'signup'.");
            return;
        }
        //limit incorrect password attempts to 3
        int count = 0;
        do{
            password = input.getInput("Password: ");
            if(userRepository.notMatchesPassword(userName, password)){
                printer.printError("Incorrect password.");
                count++;
            }
            if(count==3){
                printer.printError("Try after some time!");
                //todo dont allow the user to signin for a specific amount of time. display a message so.
                return;
            }
        }while (userRepository.notMatchesPassword(userName, password));
        userRepository.updateStatus(userName);
        printer.printSuccess("Login successful.");
        //remove from deactivated list
        if(userRepository.isDeactivatedUser(userRepository.getUser(userName))){
            userRepository.activateUser(userRepository.getUser(userName));
        }
        printer.printContent("List of commands available for normal users\n" +
                "1. 'tweet' - to post a tweet\n" +
                "2. 'like' - to like a tweet\n" +
                "3. 'comment' - to comment on a tweet\n" +
                "4. 'retweet' - to retweet any tweet\n" +
                "5. 'search' - to search any user\n" +
                "6. 'profile' - to view profile of any user\n" +
                "7. 'follow' - to follow a user\n" +
                "8. 'unfollow' - to unfollow a user, you are already following\n" +
                "9. 'feed' - to view feed page\n" +
                "10. 'delete' - to delete a tweet or a comment\n" +
                "11. 'update' - to update your own profile fields like bio, location or change password\n" +
                "12. 'notification' - to view the list of notifications\n" +
                "13. 'chat' - to send message to any user\n" +
                "14. 'apply' - to become a verified twitter user\n" +
                "15. 'deactivate' - to deactivate your account\n" +
                "16. 'signup' - to create a new account\n" +
                "17. 'signin' - to signin to your account\n" +
                "18. 'signout' - to signout of your account\n\n" +
                "Exclusive command available for verified users\n" +
                "19. 'edit' - to edit a tweet\n");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
