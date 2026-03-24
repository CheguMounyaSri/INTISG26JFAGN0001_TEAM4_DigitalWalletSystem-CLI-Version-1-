package com.cognizant.digitalwalletsystem.service;
import com.cognizant.digitalwalletsystem.dao.TransactionDao;
import com.cognizant.digitalwalletsystem.model.Transaction;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDao transactionDao;

    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
    public List<Transaction> getTransactionsByWalletId(int walletId)throws SQLException{
        return transactionDao.findByWalletId(walletId);
    }
}
