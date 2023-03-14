package ui.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserRegister {
    private static final Scanner scanner = new Scanner(System.in);
    public String[] getDetails(){
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.println("Date of Birth");
        System.out.print("Day: ");
        int day = scanner.nextInt();
        System.out.print("Month: ");
        int month = scanner.nextInt();
        System.out.print("Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        String dob = date.format(formatter);
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();
        System.out.print("User name: ");
        String userName = scanner.nextLine();
        System.out.println("Account has been successfully created! Sign in to continue. Use the command 'signin'.");
        return new String[]{name, email, dob, password, userName, "normal", "out"};
        //todo for verified users change it to verified user
    }


}
