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
            printer.printContent("Signin first. Use the command 'signin'.");
            return;
        }
        //todo instead of searching by username search by both name and username
        GetInput input = new GetInput();
        String userNameToSearch = input.getInput("Username: ");
        List<String> users = userRepository.searchResults(userNameToSearch);
        if(users.size()==0){
            printer.printContent("No results for " + userNameToSearch);
        }else{
            printer.printContent(users.toString());
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
