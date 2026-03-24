package com.cognizant.digitalwalletsystem.model;

import java.sql.Timestamp;

public class KYC {
    private int id;
    private int userId;
    private String documentType; //AADHAAR,PAN,PASSPORT
    private String status;//PENDING,APPROVED,REJECTED
    private String documentNumber;
    private Timestamp createdAt;

    public KYC(){}

    public KYC(int userId, String documentType, String documentNumber, String status) {
        this.userId = userId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
