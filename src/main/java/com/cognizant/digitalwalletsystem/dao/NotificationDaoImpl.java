package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.Notification;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class NotificationDaoImpl implements NotificationDao{
    @Override
    public void save(Notification notification) throws SQLException {
        String query = "INSERT INTO notifications (user_id,message,is_read)VALUES(?,?,?)";
        Connection conn=DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,notification.getUserId());
            stmt.setString(2,notification.getMessage());
            stmt.setBoolean(3,notification.isRead());
            stmt.executeUpdate();
        }

    }

    @Override
    public List<Notification> findByUserId(int userId) throws SQLException {
        List<Notification> notifications=new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE user_id=? AND is_deleted=FALSE ORDER BY created_at DESC";
        Connection conn =DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,userId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setUserId(rs.getInt("user_id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setRead(rs.getBoolean("is_read"));
                    notification.setCreatedAt(rs.getTimestamp("created_at"));
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    @Override
    public void markAllAsRead(int userId) throws SQLException {
        String query = "UPDATE notifications SET is_read = TRUE WHERE user_id=?";
        Connection conn  = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,userId);
            stmt.executeUpdate();
        }
    }
}
