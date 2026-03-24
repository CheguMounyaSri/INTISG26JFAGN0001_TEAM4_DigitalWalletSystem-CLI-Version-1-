package com.cognizant.digitalwalletsystem.service;

import com.cognizant.digitalwalletsystem.dao.NotificationDao;
import com.cognizant.digitalwalletsystem.model.Notification;
import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public List<Notification> getNotificationForUser(int userId) throws SQLException{
        return notificationDao.findByUserId(userId);
    }

    public void markAsRead(int userId)throws SQLException{
        notificationDao.markAllAsRead(userId);
    }
}
