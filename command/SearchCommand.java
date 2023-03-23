package command;

import model.User;
import repository.UserRepository;
import ui.input.UserInputScanner;
import ui.output.Printer;
import java.util.List;

public class SearchCommand extends Command {

    public SearchCommand(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
        this.code = "search";
    }

    @Override
    public void execute(String command) {
        User user = userRepository.getActiveUser();
        if(user == null){
            printer.printError("Signin first. Use the command 'signin'.");
            return;
        }
        UserInputScanner input = new UserInputScanner();
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
}
