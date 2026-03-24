package com.cognizant.digitalwalletsystem.service;
import com.cognizant.digitalwalletsystem.exception.*;
import com.cognizant.digitalwalletsystem.dao.*;
import com.cognizant.digitalwalletsystem.model.*;
import com.cognizant.digitalwalletsystem.strategy.TransactionStrategy;
import com.cognizant.digitalwalletsystem.util.ValidationUtil;
import java.math.BigDecimal;
import java.sql.SQLException;

public class WalletService {
    private final WalletDao walletDao;
    private final KYCDao kycDao;
    private final TransactionStrategy depositStrategy;
    private final TransactionStrategy transferStrategy;

    public WalletService(WalletDao walletDao, KYCDao kycDao, TransactionStrategy depositStrategy, TransactionStrategy transferStrategy) {
        this.walletDao = walletDao;
        this.kycDao = kycDao;
        this.depositStrategy = depositStrategy;
        this.transferStrategy = transferStrategy;
    }
    public Wallet getWalletByUserId(int userId) throws SQLException{
        return walletDao.findByUserId(userId);
    }

    public void deposit(int userId,BigDecimal amount)throws Exception{
        ValidationUtil.validateAmount(amount);

        Wallet wallet = getWalletByUserId(userId);

        if(wallet==null) {
            throw new Exception("Wallet Not Found for the user.");
        }

        depositStrategy.execute(wallet.getId(),null,amount);
    }

    public void transfer(int senderUserId,String recieverEmailOrPhone,BigDecimal amount,UserDao userDao)throws Exception{
        ValidationUtil.validateAmount(amount);

        KYC kyc = kycDao.findByUserId(senderUserId);
        if(kyc==null || !"APPROVED".equals(kyc.getStatus())){
            throw new KYCNotApprovedException("Your KYC is not approved. Transfer is disabled");
        }

        Wallet senderWallet = getWalletByUserId(senderUserId);
        if(senderWallet==null) {
            throw new Exception("Sender wallet not dound.");
        }

        User recieverUser;
        try {
            if(recieverEmailOrPhone.contains("@")){
                recieverUser=userDao.findByEmail(recieverEmailOrPhone);
            }else{
                recieverUser=userDao.findByPhone(recieverEmailOrPhone);
            }
        }catch(UserNotFoundException e){
            throw new Exception("Reciever not found with provided email/phone");
        }

        Wallet recieverWallet = getWalletByUserId(recieverUser.getId());
        if(recieverWallet==null){
            throw new Exception("Reciever wallet not found.");
        }
        transferStrategy.execute(senderWallet.getId(),recieverWallet.getId(),amount);
    }

}
