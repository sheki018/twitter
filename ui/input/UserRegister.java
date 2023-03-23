package ui.input;

import repository.UserRepository;
import ui.output.Printer;
import validation.UserValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserRegister {
    private static final UserInputScanner input = new UserInputScanner();
    private final UserRepository userRepository;
    private final Printer printer;
    public UserRegister(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }
    public String[] userDetails(){
        String name, email, dob, password, confirmPassword, userName;
        int day=Integer.MAX_VALUE, month=Integer.MAX_VALUE, year=Integer.MAX_VALUE;
        UserValidator validator = new UserValidator(userRepository, printer);
        do {
            name = input.getInput("Name: ");
        } while (!validator.validateName(name));
        do {
            email = input.getInput("Email: ");
        }while (!validator.validateEmail(email));
        printer.printContent("Date of Birth");
        do {
            boolean flag = true;
            do {
                try {
                    day = Integer.parseInt(input.getInput("Day: "));
                    flag = false;
                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                }
            } while (!validator.validateDay(day) || flag);
            flag = true;
            do {
                try {
                    month = Integer.parseInt(input.getInput("Month: "));
                    flag = false;
                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                }
            } while (!validator.validateMonth(month) || flag);
            flag = true;
            do {
                try {
                    year = Integer.parseInt(input.getInput("Year: "));
                    flag = false;
                } catch (NumberFormatException e) {
                    printer.printError("Enter a valid number.");
                }
            } while (!validator.validateYear(year) || flag);
        }while (!validator.validateDoB(day, month, year));
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        dob = date.format(formatter);
        System.out.println(dob);
        do {
            printer.printContent("Password policy:\n" +
                    "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character(@#$%^&+=).\n" +
                    "It should not contain any white space.\n" +
                    "It should be at least 8 characters long and at most 20 characters long.");
            password = input.getInput("Password: ");
        }while(validator.validatePassword(password));
        do {
            confirmPassword = input.getInput("Confirm Password: ");
        }while (validator.validatePassword(password, confirmPassword));
        do {
            userName = input.getInput("User name: ");
        }while (!validator.validateUserName(userName));
        return new String[]{name, email, dob, password, userName, "normal", "out"};
    }
}
