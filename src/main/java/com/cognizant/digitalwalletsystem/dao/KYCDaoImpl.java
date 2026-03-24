package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.KYC;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KYCDaoImpl implements KYCDao{
    @Override
    public KYC save(KYC kyc) throws SQLException {
        String query = "INSERT INTO kyc (user_id,document_type,document_number,status) VALUES (?,?,?,?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,kyc.getUserId());
            stmt.setString(2,kyc.getDocumentType());
            stmt.setString(3,kyc.getDocumentNumber());
            stmt.setString(4,kyc.getStatus());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    kyc.setId(rs.getInt(1));
                }
            }
            return kyc;
        }

    }

    @Override
    public KYC findByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM kyc WHERE user_id=? AND is_deleted=FALSE";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,userId);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToKYC(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void updateStatus(int userId, String status) throws SQLException {

        String query = "UPDATE kyc SET status=? WHERE user_id=?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,status);
            stmt.setInt(2,userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<KYC> findPending() throws SQLException {
        List<KYC> pendingList = new ArrayList<>();
        String query = "SELECT * FROM kyc WHERE status='PENDING' AND is_deleted=FALSE";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pendingList.add(mapResultSetToKYC(rs));
            }
        }
        return pendingList;
    }

    private KYC mapResultSetToKYC(ResultSet rs)throws SQLException{
        KYC kyc = new KYC();
        kyc.setId(rs.getInt("id"));
        kyc.setUserId(rs.getInt("user_id"));
        kyc.setDocumentType(rs.getString("document_type"));
        kyc.setDocumentNumber(rs.getString("document_number"));
        kyc.setStatus(rs.getString("status"));
        kyc.setCreatedAt(rs.getTimestamp("created_at"));
        return kyc;
    }
}
