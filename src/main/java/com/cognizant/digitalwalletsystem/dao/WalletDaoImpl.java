package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.Wallet;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;

public class WalletDaoImpl implements WalletDao{
    @Override
    public Wallet createWallet(int userId) throws SQLException {
        String query = "INSERT INTO wallets(user_id,balance)VALUES(?,?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1,userId);
            stmt.setBigDecimal(2,BigDecimal.ZERO);
            stmt.executeUpdate();

            Wallet wallet = new Wallet(userId,BigDecimal.ZERO);
            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    wallet.setId(rs.getInt(1));
                }
            }
            return wallet;
        }
    }

    @Override
    public Wallet findByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM wallets WHERE user_id=? AND is_deleted=FALSE";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,userId);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToWallet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Wallet findById(int id) throws SQLException {
        String query = "SELECT * FROM wallets WHERE id=? AND is_deleted=FALSE";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,id);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToWallet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void updateBalance(int walletId, BigDecimal newBalance) throws SQLException {
        String query = "UPDATE wallets SET balance=? WHERE id=?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, walletId);
            stmt.executeUpdate();
        }
    }


    private Wallet mapResultSetToWallet(ResultSet rs) throws SQLException{
    Wallet wallet = new Wallet();
    wallet.setId(rs.getInt("id"));
    wallet.setUserId(rs.getInt("user_id"));
    wallet.setBalance(rs.getBigDecimal("balance"));
    wallet.setCreatedAt(rs.getTimestamp("created_at"));
    return wallet;
    }

}
