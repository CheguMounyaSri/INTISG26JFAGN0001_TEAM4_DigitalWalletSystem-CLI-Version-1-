package com.cognizant.digitalwalletsystem.factory;

import com.cognizant.digitalwalletsystem.dao.*;
import com.cognizant.digitalwalletsystem.service.*;
import com.cognizant.digitalwalletsystem.strategy.*;


public class ServiceFactory {

    private static ServiceFactory instance;

    private final UserDao userDao;
    private final WalletDao walletDao;
    private final TransactionDao transactionDao;
    private final KYCDao kycDao;
    private final NotificationDao notificationDao;

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final KYCService kycService;
    private final NotificationService notificationService;

    private ServiceFactory() {
        this.userDao=new UserDaoImpl();
        this.walletDao=new WalletDaoImpl();
        this.transactionDao=new TransactionDaoImpl();
        this.kycDao=new KYCDaoImpl();
        this.notificationDao=new NotificationDaoImpl();


        TransactionStrategy depositStrategy = new DepositStrategy(walletDao,transactionDao,notificationDao);
        TransactionStrategy transferStrategy = new TransferStrategy(walletDao,transactionDao,notificationDao);

        this.userService=new UserService(userDao,walletDao,notificationDao);
        this.walletService=new WalletService(walletDao,kycDao,depositStrategy,transferStrategy);
        this.transactionService=new TransactionService(transactionDao);
        this.kycService=new KYCService(kycDao);
        this.notificationService=new NotificationService(notificationDao);
    }

    public static synchronized ServiceFactory getInstance(){
        if(instance==null){
            instance = new ServiceFactory();
        }
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public KYCService getKycService() {
        return kycService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
