package model;

import java.util.Date;

public class Notification {
    private String userName;
    private String notification;
    private Date notificationDate;
    public Notification(String userName, String notification){
        this.userName = userName;
        this.notification = notification;
        this.notificationDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}