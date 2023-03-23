import command.CommandExecutor;
import repository.UserRepository;
import ui.output.Printer;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        UserRepository userRepository = UserRepository.getInstance();
        Printer printer = new Printer(System.out);
        CommandExecutor commandExecutor = new CommandExecutor(userRepository, printer);
        System.out.println("Welcome to \u001B[34m" + "Twitter" + "\u001B[0m. Create an account first. Use" + "\u001B[1m'signup'" + "\u001B[0m command.");
        int flag = 0;

        while (true){
            System.out.print("~");
            String input = scanner.nextLine();
            flag++;
            if(flag==1 && !input.trim().equalsIgnoreCase("signup")){
                System.out.println("Please register first. Use the command " + "\u001B[1m'signup'" + "\u001B[0m to start your registration process.");
                flag=0;
                continue;
            }
            commandExecutor.executeCommand(input);
        }
    }
}
/*
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_LIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNBOLD = "\u001B[21m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_STOP_UNDERLINE = "\u001B[24m";
    public static final String ANSI_BLINK = "\u001B[5m";
*/
