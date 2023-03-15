package ui.input;

import java.util.Scanner;

public class GetInput {
    private final Scanner scanner = new Scanner(System.in);
    public String getInput(String info){
        System.out.print(info);
        return scanner.nextLine();
    }
}
