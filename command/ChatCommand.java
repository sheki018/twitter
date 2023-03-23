package command;

import model.Message;
import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;

import java.util.List;

public class ChatCommand extends Command{

    public ChatCommand(UserRepository userRepository, Printer displayMessage){
        super(userRepository, displayMessage);
        this.code = "chat";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        String sender = userRepository.getUserName(user);
        UserInputScanner input = new UserInputScanner();
        String person = input.getInput("Who do you want to chat with? @");
        if(userRepository.isDeactivatedUser(userRepository.getUser(person))){
            printer.printError("User no longer available.");
            return;
        }
        if(userRepository.noUserName(person)){
            printer.printError("No such user found.");
            return;
        }
        List<Message> messages = user.getMessages();
        if(person.equalsIgnoreCase(sender)){
            if(messages.size()==0){
                printer.printContent("This your personal space. Put in all important notes here! :)");
            }
            for (Message content: messages) {
                printer.printMessage(content);
            }
            String message = input.getInput("I say: ");
            user.addMessage(sender, message);
            return;
        }
        if(messages.size()==0){
            printer.printContent("Start your conversation.");
        }
        User receiver = userRepository.getUser(person);
        for (Message content: messages) {
            printer.printMessage(content);
        }
        String message = input.getInput("@" + sender +" says: ");
        receiver.addMessage(sender, message);
        user.addMessage(sender, message);
        receiver.addNotifications("@"+sender+" sent you a message");
    }
}
