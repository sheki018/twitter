package command;

public interface Command {
    //initialize code here
    void execute(String command);

    boolean matches(String input);
}
