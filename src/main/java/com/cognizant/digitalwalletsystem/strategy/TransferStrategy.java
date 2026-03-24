package com.cognizant.digitalwalletsystem.strategy;
import com.cognizant.digitalwalletsystem.dao.NotificationDao;
import com.cognizant.digitalwalletsystem.dao.TransactionDao;
import com.cognizant.digitalwalletsystem.dao.WalletDao;
import com.cognizant.digitalwalletsystem.exception.InsufficientBalanceException;

import com.cognizant.digitalwalletsystem.model.Notification;
import com.cognizant.digitalwalletsystem.model.Transaction;
import com.cognizant.digitalwalletsystem.model.Wallet;
import com.cognizant.digitalwalletsystem.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;

public class TransferStrategy implements TransactionStrategy{

    private final WalletDao walletDao;
    private final TransactionDao transactionDao;
    private final NotificationDao notificationDao;


    public TransferStrategy(WalletDao walletDao, TransactionDao transactionDao, NotificationDao notificationDao) {
        this.walletDao = walletDao;
        this.transactionDao = transactionDao;
        this.notificationDao = notificationDao;
    }


    @Override
    public void execute(int senderWalletId, Integer recieverWalletId, BigDecimal amount) throws Exception {
        if (recieverWalletId == null) {
            throw new Exception("Reciver wallet ID is required for transfer");
        }

        if (senderWalletId == recieverWalletId) {
            throw new Exception("Cannot transfer to the same wallet.");
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        boolean originalAutoCommit= conn.getAutoCommit();
        try{
         conn.setAutoCommit(false);
        Wallet senderWallet = walletDao.findById(senderWalletId);
        Wallet recieverWallet = walletDao.findById(recieverWalletId);


        if (senderWallet == null || recieverWallet == null) {
            throw new Exception("Sender or Reciever wallet not found");
        }

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        BigDecimal newSenderBalance = senderWallet.getBalance().subtract(amount);
        walletDao.updateBalance(senderWalletId, newSenderBalance);


        BigDecimal newRecieverBalance = recieverWallet.getBalance().add(amount);
        walletDao.updateBalance(recieverWalletId, newRecieverBalance);

        Transaction senderTx = new Transaction(senderWalletId, "TRANSFER", amount, "SUCCESS", "Transfer to wallet " + recieverWalletId);
        transactionDao.save(senderTx);

        Transaction recieverTx = new Transaction(recieverWalletId, "CREDIT", amount, "SUCCESS", "Recieved from wallet " + senderWalletId);
        transactionDao.save(recieverTx);


        notificationDao.save(new Notification(senderWallet.getUserId(), "Transferred Rs " + amount + " to wallet Id " + recieverWalletId));
        notificationDao.save(new Notification(recieverWallet.getUserId(), "Recieved Rs " + amount + " from wallet Id " + senderWalletId));

        conn.commit();

    }catch(Exception e) {
            conn.rollback();

            Transaction failedTx = new Transaction(senderWalletId, "TRANSFER", amount, "FAILED", "Transfer failed " + e.getMessage());
            try {

            } catch (Exception ignore) {
            }
            throw e;
        } finally {
                conn.setAutoCommit(originalAutoCommit);
            }

        }
    }

