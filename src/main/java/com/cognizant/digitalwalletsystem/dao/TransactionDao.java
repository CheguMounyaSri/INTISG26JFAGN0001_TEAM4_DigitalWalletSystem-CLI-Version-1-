package com.cognizant.digitalwalletsystem.dao;

import com.cognizant.digitalwalletsystem.model.Transaction;
import java.util.List;
import java.sql.SQLException;

public interface TransactionDao {
    void save(Transaction transaction)throws SQLException;
    List<Transaction> findByWalletId(int walletId)throws SQLException;
}
