package com.example.coen_mp_concordiatravelwebapplication.models.notficationModels;

import java.sql.Timestamp;

public class Notification {

    private String notificationId;
    private String userId;

    private String message;

    private Timestamp timeOfNotification;

    public Notification(String userId, String message, Timestamp timeOfNotification){
        this.userId = userId;
        this.message = message;
        this.timeOfNotification = timeOfNotification;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimeOfNotification() {
        return timeOfNotification;
    }

    public void setTimeOfNotification(Timestamp timeOfNotification) {
        this.timeOfNotification = timeOfNotification;
    }
}
