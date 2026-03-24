package com.cognizant.digitalwalletsystem.strategy;
import com.cognizant.digitalwalletsystem.dao.NotificationDao;
import com.cognizant.digitalwalletsystem.dao.TransactionDao;
import com.cognizant.digitalwalletsystem.dao.WalletDao;
import com.cognizant.digitalwalletsystem.model.Notification;
import com.cognizant.digitalwalletsystem.model.Transaction;
import com.cognizant.digitalwalletsystem.model.Wallet;
import java.math.BigDecimal;

public class DepositStrategy implements TransactionStrategy {

    private final WalletDao walletDao;
    private final TransactionDao transactionDao;
    private final NotificationDao notificationDao;

    public DepositStrategy(WalletDao walletDao, TransactionDao transactionDao, NotificationDao notificationDao) {
        this.walletDao = walletDao;
        this.transactionDao = transactionDao;
        this.notificationDao = notificationDao;
    }

    @Override
    public void execute(int walletId, Integer recieverWalletId, BigDecimal amount) throws Exception {
        Wallet wallet = walletDao.findById(walletId);
        if(wallet == null){
            throw new Exception("Wallet not found");
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        walletDao.updateBalance(walletId,newBalance);

        Transaction transaction=new Transaction(walletId,"CREDIT",amount,"SUCCESS","Deposit of "+amount);
        transactionDao.save(transaction);

        notificationDao.save(new Notification(wallet.getUserId(),"Deposited Rs"+amount+" to your wallet. New Balance: "+newBalance));


    }
}
