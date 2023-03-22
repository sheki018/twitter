package model;

public class Notification extends UserAction{
    private String notification;

    public Notification(String userName, String notification) {
        super(userName);
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}