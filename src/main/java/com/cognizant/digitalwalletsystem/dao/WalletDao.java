package com.cognizant.digitalwalletsystem.dao;
import com.cognizant.digitalwalletsystem.model.Wallet;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface WalletDao {
    Wallet createWallet(int userId)throws SQLException;
    Wallet findByUserId(int userId)throws SQLException;
    Wallet findById(int id)throws SQLException;
    void updateBalance(int walletId,BigDecimal newBalance)throws SQLException;
}
