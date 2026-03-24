package com.cognizant.digitalwalletsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/digital_wallet";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection= DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException|SQLException e){
            System.out.println("Database connection failed: "+e.getMessage());
            throw new RuntimeException("Database connection error",e);
        }
    }

    public static synchronized DatabaseConnection getInstance(){
        if(instance==null || isConnectionClosed(instance.connection)){
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Reconnect failed", e);
        }
        return connection;
    }

    private static boolean isConnectionClosed(Connection conn) {
        try {
            return conn==null || conn.isClosed();
        }catch(SQLException e) {
          return true;
        }
    }

}
