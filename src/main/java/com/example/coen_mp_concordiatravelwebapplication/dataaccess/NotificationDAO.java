package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.notficationModels.Notification;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;

import java.util.List;

public interface NotificationDAO {

    void sendNotificationToUser(String userId, String message);

    Boolean sendNotificationToAllCustomers(String message);

    Boolean sendNotificationToAllAgents(String message);

    List<Notification> getNotificationsByUserId(String userId);

    Boolean deleteNotification(String notificationId);
}
