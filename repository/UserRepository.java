package repository;

import model.User;
import model.VerifiedUser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepository {
    private final Map<String, User> userMap = new HashMap<>();
    private final Map<User, String[]> userDetails = new HashMap<>();
    private final Map<User, Date> deactivatedUsers = new HashMap<>();

    public void registerUser(String[] details){
        String userName = details[4];
        User user = new User(userName);
        userMap.put(userName, user);
        userDetails.put(user, details);
    }

    public void deactivateUser(User user, Date date){
        deactivatedUsers.put(user,date);
    }

    public boolean isDeactivatedUser(User user){
        return deactivatedUsers.containsKey(user);
    }

    public Date deactivatedTime(User user){return deactivatedUsers.get(user);}
    public void activateUser(User user){
        deactivatedUsers.remove(user);
    }
    //todo use thread to delete users permanently in the background
    public void deleteUser(User user){
        deactivatedUsers.remove(user);
        userMap.remove(getUserName(user));
        userDetails.remove(user);
    }
    public boolean noUserName(String userName) {
        User user = userMap.get(userName);
        return user == null;
    }

    public boolean notMatchesPassword(String userName, String password) {
        User user = userMap.get(userName);
        String[] details = userDetails.get(user);
        return !password.equals(details[3]);
    }

    public void updateStatus(String userName) {
        User user = userMap.get(userName);
        userDetails.get(user)[6] = "active";
    }

    public User getUser(String userName){
        return userMap.get(userName);
    }

    public String getName(User user){
        String name = null;
        for(Map.Entry<User, String[]> entry: userDetails.entrySet()) {
            if(entry.getKey()==user){
                name=entry.getValue()[0];
            }
        }
        return name;
    }
    public boolean getEmail(String mail){
        for(Map.Entry<User, String[]> entry: userDetails.entrySet()) {
            if(entry.getValue()[1].equals(mail)){
                return true;
            }
        }
        return false;
    }
    public User getActiveUser() {
        User user = null;
        for(Map.Entry<User, String[]> entry: userDetails.entrySet()) {
            String[] details = entry.getValue();
            if(details[6].equals("active")){
                user = entry.getKey();
            }
        }
        return user;
    }

    public void updatePassword(String userName, String newPassword) {
        User user = userMap.get(userName);
        userDetails.get(user)[3] = newPassword;
    }

    public String getUserName(User user) {
        String userName=null;
        for(Map.Entry<String, User> entry: userMap.entrySet()) {
            if(entry.getValue() == user) {
                userName = entry.getKey();
                break;
            }
        }
        return userName;
    }

    public void signoutUser(User user) {
        userDetails.get(user)[6]="out";
    }

    public List<String> searchResults(String userName) {
        Pattern regex = Pattern.compile(userName, Pattern.CASE_INSENSITIVE);
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, User> entry: userMap.entrySet()) {
            String match = entry.getKey();
            if(isDeactivatedUser(getUser(match))){
                continue;
            }
            Matcher matcher = regex.matcher(match);
            if(matcher.find()){
                result.add(match);
            }
        }
        return result;
    }

    public String getAccountType(User user){
        return userDetails.get(user)[5];
    }
    public void verifyUser(User user){
        VerifiedUser verifiedUser = new VerifiedUser(getUserName(user));
        userDetails.get(user)[5]="verified";
    }
}