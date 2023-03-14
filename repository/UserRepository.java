package repository;

import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepository {
    private final Map<String, User> userMap = new HashMap<>();
    private final Map<User, String[]> userDetails = new HashMap<>();
    private final Map<String, User> deactivatedUsers = new HashMap<>();

    public void registerUser(String[] details){
        String userName = details[4];
        User user = new User(userName);
        userMap.put(userName, user);
        userDetails.put(user, details);
    }

    public void deactivateUser(User user){
        deactivatedUsers.put(getUserName(user),user);
    }

    public boolean isDeactivatedUser(String userName){
        return deactivatedUsers.containsKey(userName);
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
        userDetails.get(user)[5] = "active";
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

    public User getActiveUser() {
        User user = null;
        for(Map.Entry<User, String[]> entry: userDetails.entrySet()) {
            String[] details = entry.getValue();
            if(details[5].equals("active")){
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
        userDetails.get(user)[5]="out";
    }

    public List<String> searchResults(String userName) {
        Pattern regex = Pattern.compile(userName, Pattern.CASE_INSENSITIVE);
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, User> entry: userMap.entrySet()) {
            String match = entry.getKey();
            Matcher matcher = regex.matcher(match);
            if(matcher.find()){
                result.add(match);
            }
        }
        return result;
    }
}