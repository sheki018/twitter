package command;

import ui.output.Printer;

public class InvalidCommand implements Command{
    private final Printer tweetViewer;

    public InvalidCommand(Printer tweetViewer){
        this.tweetViewer = tweetViewer;
    }
    @Override
    public void execute(String command) {
        tweetViewer.printError(String.format("Command: %s is invalid!", command));
    }

    @Override
    public boolean matches(String input) {
        return false;
    }
}
