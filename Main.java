import command.CommandExecutor;
import repository.UserRepository;
import ui.output.Printer;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        Printer printer = new Printer(System.out);
        CommandExecutor commandExecutor = new CommandExecutor(userRepository, printer);
        System.out.println("Welcome to Twitter. Create an account first. Use 'signup' command");
        //System.out.println("\u001B[31m" + "and now the text is red" + "\u001B[0m");
        int flag = 0;

        while (true){
            System.out.print("~");
            String input = scanner.nextLine();
            flag++;
            if(flag==1 && !input.trim().equalsIgnoreCase("signup")){
                System.out.println("Please register first. Use the command 'signup' to start your registration process.");
                flag=0;
                continue;
            }
            commandExecutor.executeCommand(input);
        }
    }
}