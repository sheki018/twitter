package ui.input;

import java.util.Scanner;

public class GetInput {
    private Scanner scanner = new Scanner(System.in);
    public String getInput(String info){
        System.out.print(info);
        String input = scanner.nextLine();
        return input;
    }
}
