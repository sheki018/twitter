package command;

import model.User;
import repository.UserRepository;
import ui.input.GetInput;
import ui.output.Printer;
import java.util.List;

public class SearchCommand implements Command{
    private static final String code = "search";
    private final UserRepository userRepository;
    private final Printer printer;

    public SearchCommand(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        GetInput input = new GetInput();
        String userNameToSearch = input.getInput("Username: ");
        List<String> users = userRepository.searchResults(userNameToSearch);
        if(users.size()==0){
            printer.printError("No results for " + userNameToSearch);
            return;
        }else{
            printer.printSuccess(users.toString());
        }
        printer.printContent("To view or follow these users you can use the commands 'profile' or 'follow'.");
    }

    @Override
    public boolean matches(String input) {
        if(input!=null && !input.isEmpty()){
            return input.trim().equalsIgnoreCase(code);
        }
        return false;
    }
}
