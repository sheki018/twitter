package command;

import model.Notification;
import model.User;
import repository.UserRepository;
import ui.output.Printer;

import java.util.List;

public class NotificationCommand extends Command {

    public NotificationCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "notification";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        List<Notification> notifications = user.viewNotifications();
        if(notifications.size()==0){
            printer.printError("No notifications to display.");
            return;
        }
        notifications.sort((t1,t2) -> t2.getDate().compareTo(t1.getDate()));
        for (Notification notification:
             notifications) {
            printer.printNotification(notification);
        }
    }
}
