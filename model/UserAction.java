package model;

import java.util.Date;

public class UserAction {
    private String userName;
    private Date actionDate;
    public UserAction(String userName){
        this.userName = userName;
        this.actionDate = new Date();
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return actionDate;
    }

    public void setDate(Date messageDate) {
        this.actionDate = messageDate;
    }
}
