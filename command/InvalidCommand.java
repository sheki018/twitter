package command;

import repository.UserRepository;
import ui.output.Printer;

public class InvalidCommand extends Command{

    public InvalidCommand(UserRepository userRepository, Printer printer){
        super(userRepository, printer);
    }
    public void execute(String command) {
        printer.printError(String.format("Command: %s is invalid!", command));
    }
}