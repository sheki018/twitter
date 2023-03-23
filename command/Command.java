package command;

public interface Command {
    void execute(String command);

    boolean matches(String input);
}
