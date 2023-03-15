package command;

import model.Notification;
import model.User;
import repository.UserRepository;
import ui.output.Printer;

import java.util.List;

public class NotificationCommand implements Command{
    private static final String code = "notification";
    private final UserRepository userRepository;
    private final Printer displayMessage;

    public NotificationCommand(UserRepository userRepository, Printer displayMessage){
        this.userRepository = userRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            displayMessage.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        List<Notification> notifications = user.viewNotifications();
        if(notifications.size()==0){
            displayMessage.printContent("No notifications to display.");
            return;
        }
        notifications.sort((t1,t2) -> t2.getNotificationDate().compareTo(t1.getNotificationDate()));
        for (Notification notification:
             notifications) {
            displayMessage.printNotification(notification);
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
