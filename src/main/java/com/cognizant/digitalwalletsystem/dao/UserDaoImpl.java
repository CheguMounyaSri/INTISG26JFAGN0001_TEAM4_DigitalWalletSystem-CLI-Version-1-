package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.exception.DuplicateUserException;
import com.cognizant.digitalwalletsystem.exception.UserNotFoundException;
import com.cognizant.digitalwalletsystem.model.User;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.sql.*;

public class UserDaoImpl implements UserDao{
    @Override
    public User save(User user) throws DuplicateUserException{
        String query = "INSERT INTO users(name,email,phone,password_hash)VALUES(?,?,?,?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,user.getName());
            stmt.setString(2,user.getEmail());
            stmt.setString(3,user.getPhone());
            stmt.setString(4,user.getPasswordHash());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows>0){
                try(ResultSet rs=stmt.getGeneratedKeys()){
                    if(rs.next()){
                        user.setId(rs.getInt(1));
                    }
                }
            }
            return user;
        }catch(SQLIntegrityConstraintViolationException e){
            throw new RuntimeException("Email or phone already registered");
        }catch(SQLException e){
            throw new RuntimeException("Database error saving user",e);
        }
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        String query="SELECT * FROM users WHERE email=? AND is_deleted=FALSE";
        return findUserByQuery(query,email);
    }

    @Override
    public User findByPhone(String phone) throws UserNotFoundException {
        String query="SELECT * FROM users WHERE phone=? AND is_deleted=FALSE";
        return findUserByQuery(query,phone);
    }

    @Override
    public User findById(int id) throws UserNotFoundException {
        String query = "SELECT * FROM users WHERE id=? AND is_deleted=FALSE";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,id);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToUser(rs);
                }
            }
        }catch(SQLException e){
            throw new RuntimeException("Database error finding user by ID",e);
        }
        throw new UserNotFoundException("User not found with ID "+id);
    }

    private User findUserByQuery(String query,String param)throws UserNotFoundException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error finding user", e);
        }
        throw new UserNotFoundException("User not found " + param);
    }

    private User mapResultSetToUser(ResultSet rs)throws SQLException{
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;

    }

    @Override
    public void updatePassword(int userId, String newPasswordHash) throws Exception {
        String query = "UPDATE users SET password_hash=? WHERE id=?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,newPasswordHash);
            stmt.setInt(2,userId);
            stmt.executeUpdate();
        }

    }
}
