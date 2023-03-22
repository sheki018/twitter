package validation;

import repository.UserRepository;
import ui.output.Printer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UserValidator extends Validator{

    public UserValidator(UserRepository userRepository, Printer printer) {
        super(userRepository, printer);
    }

    public boolean validateName(String name){
        if(isBlank(name)){
            printer.printError("Name cannot be empty.");
            return false;
        }else if(matchRegex("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", name)){
            printer.printError("Provide a valid name.");
            return false;
        }else if(name.length()>50){
            printer.printError("Name cannot exceed 50 characters.");
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email){
        if(userRepository.getEmail(email)){
            printer.printError("This email is already associated with another account.");
            return false;
        }else if (isBlank(email)){
            printer.printError("Email address cannot be empty.");
            return false;
        } else if (matchRegex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", email)){
            printer.printError("Provide a valid email address.");
            return false;
        }
        return true;
    }
    public boolean validateDay(int day){
        if(!(day>=1&&day<=31)){
            printer.printError("Invalid day.");
            return false;
        }
        return true;
    }
    public boolean validateMonth(int month){
        if(!(month>=1&&month<=12)){
            printer.printError("Invalid month.");
            return false;
        }
        return true;
    }
    public boolean validateYear(int year){
        if(year > LocalDate.now().getYear()){
            printer.printError("Invalid year.");
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
            printer.printError("Invalid date.");
            return false;
        }
    }
    public boolean validatePassword(String password) {
        if(password.length()<8){
            printer.printError("Minimum length is 8 characters.");
            return true;
        }else if(isBlank(password)){
            printer.printError("Password cannot be empty.");
            return true;
        }else if(matchRegex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", password)){
            printer.printError("Password does not abide by the password policy.");
            return true;
        }
        return false;
    }
    public boolean validatePassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            printer.printError("Password doesn't match.");
            return true;
        }
        return false;
    }
    public boolean validateUserName(String userName){
        if(userName.length()>15){
            printer.printError("User name cannot exceed 15 characters.");
            return false;
        }else if(isBlank(userName)){
            printer.printError("User name cannot be blank.");
            return false;
        }else if(!userRepository.noUserName(userName)){
            printer.printError("User name already exists.");
            return false;
        }else if(matchRegex("^[A-Za-z0-9_]{1,15}$", userName)){
            printer.printError("User name can be a combination of either letters or numbers or underscores.");
            return false;
        }
        return true;
    }
    private boolean matchRegex(String regex, String input){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return !m.matches();
    }

}
