package validation;

import repository.UserRepository;
import ui.output.Printer;

public class Validator {
    protected final UserRepository userRepository;
    protected final Printer printer;
    public Validator(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }
    public boolean isBlank(String input){
        return input.trim().isEmpty();
    }
}
