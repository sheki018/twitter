package validation;

import repository.UserRepository;
import ui.output.Printer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private final UserRepository userRepository;
    private final Printer printer;

    public UserValidator(UserRepository userRepository, Printer printer){
        this.userRepository = userRepository;
        this.printer = printer;
    }
    public boolean validateName(String name){
        if(isBlank(name)){
            printer.printContent("Name cannot be empty.");
            return false;
        }else if(!matchRegex("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", name)){
            printer.printContent("Provide a valid name.");
            return false;
        }else if(name.length()>50){
            printer.printContent("Name cannot exceed 50 characters.");
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email){
        if(userRepository.getEmail(email)){
            printer.printContent("This email is already associated with another account.");
            return false;
        }else if (isBlank(email)){
            printer.printContent("Email address cannot be empty.");
            return false;
        } else if (!matchRegex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", email)){
            printer.printContent("Provide a valid email address.");
            return false;
        }
        return true;
    }
    public boolean validateDay(int day){
        if(!(day>=1&&day<=31)){
            printer.printContent("Invalid day.");
            return false;
        }
        return true;
    }
    public boolean validateMonth(int month){
        if(!(month>=1&&month<=12)){
            printer.printContent("Invalid month.");
            return false;
        }
        return true;
    }
    public boolean validateYear(int year){
        if(year > LocalDate.now().getYear()){
            printer.printContent("Invalid year.");
            return false;
        }
        return true;
    }
    public boolean validateDoB(int day, int month, int year){
        try{
            LocalDate date = LocalDate.of(year, month, day);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
            date.format(formatter);
            return true;
        }catch (DateTimeException e){
            printer.printContent("Invalid date.");
            return false;
        }
    }
    public boolean validatePassword(String password) {
        if(password.length()<8){
            printer.printContent("Minimum length is 8 characters.");
            return false;
        }else if(isBlank(password)){
            printer.printContent("Password cannot be empty.");
            return false;
        }else if(!matchRegex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", password)){
            printer.printContent("Password does not abide by the password policy.");
            return false;
        }
        return true;
    }
    public boolean validatePassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            printer.printContent("Password doesn't match.");
            return false;
        }
        return true;
    }
    public boolean validateUserName(String userName){
        if(userName.length()>15){
            printer.printContent("User name cannot exceed 15 characters.");
            return false;
        }else if(isBlank(userName)){
            printer.printContent("User name cannot be blank.");
            return false;
        }else if(!userRepository.noUserName(userName)){
            printer.printContent("User name already exists.");
            return false;
        }else if(!matchRegex("^[A-Za-z0-9_]{1,15}$", userName)){
            printer.printContent("User name can be a combination of either letters or numbers or underscores.");
            return false;
        }
        return true;
    }
    public boolean isBlank(String input){
        return input.trim().isEmpty();
    }
    public boolean isAlpha(String input){
        char[] characters = input.toCharArray();
        for(char character: characters){
            if(!Character.isLetter(character)){
                return false;
            }
        }
        return true;
    }
    private boolean matchRegex(String regex, String input){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }

}
