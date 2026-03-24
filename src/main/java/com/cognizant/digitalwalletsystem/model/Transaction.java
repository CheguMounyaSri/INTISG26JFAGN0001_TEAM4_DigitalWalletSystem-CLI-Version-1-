package com.cognizant.digitalwalletsystem.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    private int id;
    private int walletId;
    private String type;
    private BigDecimal amount;
    private String status;
    private String description;
    private Timestamp createdAt;

    public Transaction(){}

    public Transaction(int walletId, String type, BigDecimal amount, String status, String description) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
