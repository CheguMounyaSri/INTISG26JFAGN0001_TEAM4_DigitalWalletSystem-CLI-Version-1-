package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.Transaction;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao{
    @Override
    public void save(Transaction transaction) throws SQLException {
     String query = "INSERT INTO transactions (wallet_id,type,amount,status,description) VALUES(?,?,?,?,?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1,transaction.getWalletId());
            stmt.setString(2,transaction.getType());
            stmt.setBigDecimal(3,transaction.getAmount());
            stmt.setString(4,transaction.getStatus());
            stmt.setString(5,transaction.getDescription());
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    transaction.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Transaction> findByWalletId(int walletId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE wallet_id=? AND is_deleted=FALSE ORDER BY created_at DESC";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,walletId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Transaction t =new Transaction();
                    t.setId(rs.getInt("id"));
                    t.setWalletId(rs.getInt("wallet_id"));
                    t.setType(rs.getString("type"));
                    t.setAmount(rs.getBigDecimal("amount"));
                    t.setStatus(rs.getString("status"));
                    t.setDescription(rs.getString("description"));
                    t.setCreatedAt(rs.getTimestamp("created_at"));
                    transactions.add(t);

                   }
                }
            }

        return transactions;
    }
}
