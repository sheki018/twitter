package validation;

import repository.UserRepository;

public class UserValidator {
    UserRepository userRepository = new UserRepository();
    public boolean validateName(String name){
        if(name.equals("")){

        }
        return false;
    }

    public boolean validateUserName(String userName, String command){
        if(command.equals("signup")){

        }
        if(command.equals("signin")){

        }
        return false;
    }
    public boolean isBlank(String input){
        return input.trim().isEmpty();
    }
}
