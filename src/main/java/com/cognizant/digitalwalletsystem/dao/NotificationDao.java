package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.Notification;
import java.sql.SQLException;
import java.util.List;

public interface NotificationDao {
    void save(Notification notification)throws SQLException;
    List<Notification> findByUserId(int userId)throws SQLException;
    void markAllAsRead(int userId) throws SQLException;
}
