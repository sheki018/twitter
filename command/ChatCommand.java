package command;

import model.Message;
import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;

import java.util.List;

public class ChatCommand implements Command{
    private static final String code = "chat";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public ChatCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            displayMessage.printError("Signin first. Use the command 'signin'.");
            return;
        }
        String sender = userRepository.getUserName(user);
        GetInput input = new GetInput();
        String person = input.getInput("Who do you want to chat with? @");
        if(userRepository.isDeactivatedUser(userRepository.getUser(person))){
            displayMessage.printError("User no longer available.");
            return;
        }
        if(userRepository.noUserName(person)){
            displayMessage.printError("No such user found.");
            return;
        }
        List<Message> messages = user.getMessages();
        if(person.equalsIgnoreCase(sender)){
            if(messages.size()==0){
                displayMessage.printContent("This your personal space. Put in all important notes here! :)");
            }
            for (Message content: messages) {
                displayMessage.printMessage(content);
            }
            String message = input.getInput("I say: ");
            user.addMessage(sender, message);
            return;
        }
        if(messages.size()==0){
            displayMessage.printContent("Start your conversation.");
        }
        User receiver = userRepository.getUser(person);
        for (Message content: messages) {
            displayMessage.printMessage(content);
        }
        String message = input.getInput("@" + sender +" says: ");
        receiver.addMessage(sender, message);
        user.addMessage(sender, message);
        receiver.addNotifications("@"+sender+" sent you a message");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
