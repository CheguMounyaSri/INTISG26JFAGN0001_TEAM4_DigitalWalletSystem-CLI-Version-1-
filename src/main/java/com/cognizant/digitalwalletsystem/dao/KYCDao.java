package com.cognizant.digitalwalletsystem.dao;
import com.cognizant.digitalwalletsystem.model.KYC;
import java.sql.SQLException;
import java.util.List;

public interface KYCDao {
    KYC save(KYC kyc)throws SQLException;
    KYC findByUserId(int userId)throws SQLException;
    void updateStatus(int userId,String status)throws SQLException;
    List<KYC> findPending() throws SQLException;

}
