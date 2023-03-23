package command;

import repository.UserRepository;
import ui.output.Printer;

public class BaseCommand {
    protected final UserRepository userRepository;
    protected final Printer printer;

    public BaseCommand(UserRepository userRepository, Printer printer) {
        this.userRepository = userRepository;
        this.printer = printer;
    }
}
