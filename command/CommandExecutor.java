package command;

import repository.UserRepository;
import ui.output.Printer;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    private final List<Command> commands = new ArrayList<>();
    private final Command invalidCommand;

    public CommandExecutor(UserRepository userRepository, Printer printer) {
        commands.add(new SignUpCommand(userRepository, printer));
        commands.add(new SignInCommand(userRepository, printer));
        commands.add(new FeedCommand(userRepository, printer));
        commands.add(new TweetCommand(userRepository, printer));
        commands.add(new LikeCommand(userRepository, printer));
        commands.add(new CommentCommand(userRepository, printer));
        commands.add(new RepostCommand(userRepository, printer));
        commands.add(new DeleteCommand(userRepository, printer));
        commands.add(new SearchCommand(userRepository, printer));
        commands.add(new UnFollowCommand(userRepository,printer));
        commands.add(new FollowCommand(userRepository, printer));
        commands.add(new ViewProfileCommand(userRepository, printer));
        commands.add(new UpdateProfileCommand(userRepository, printer));
        commands.add(new ChatCommand(userRepository, printer));
        commands.add(new NotificationCommand(userRepository, printer));
        commands.add(new ApplyForVerificationCommand(userRepository, printer));
        commands.add(new DeactivateCommand(userRepository, printer));
        commands.add(new SignOutCommand(userRepository, printer));
        this.invalidCommand = new InvalidCommand(printer);
    }

    public void executeCommand(String commandInput){
        Command command = getCommand(commandInput);
        command.execute(commandInput);
    }
    public Command getCommand(String commandInput){
        for(Command command : commands){
            if(command.matches(commandInput)){
                return command;
            }
        }
        return invalidCommand;
    }
}
