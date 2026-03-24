package com.cognizant.digitalwalletsystem.strategy;
import java.math.BigDecimal;

public interface TransactionStrategy {

    void execute(int senderWalletId,Integer recieverWalletId,BigDecimal amount)throws Exception;
}
