package command;

import repository.UserRepository;
import ui.output.Printer;

public abstract class Command {
    protected String code = null;
    protected final UserRepository userRepository;
    protected final Printer printer;
    public Command(UserRepository userRepository, Printer printer) {
        this.userRepository = userRepository;
        this.printer = printer;
    }
    abstract void execute(String command);
    boolean matches(String input){
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
