package command;

import model.Notification;
import model.User;
import repository.UserRepository;
import ui.output.Printer;

import java.util.List;

public class NotificationCommand extends BaseCommand implements Command{
    private static final String code = "notification";

    public NotificationCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
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

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
