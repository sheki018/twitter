package command;

public interface Command {
    // todo initialize code here
    void execute(String command);

    boolean matches(String input);
}
