package com.cognizant.digitalwalletsystem.service;
import com.cognizant.digitalwalletsystem.dao.KYCDao;
import com.cognizant.digitalwalletsystem.exception.InvalidInputException;
import com.cognizant.digitalwalletsystem.model.KYC;

import java.sql.SQLException;
import java.util.List;

public class KYCService {
    private final KYCDao kycDao;

    public KYCService(KYCDao kycDao){
        this.kycDao=kycDao;
    }

    public KYC submitKYC(int userId,String documentType,String documentNumber) throws Exception{
        if(documentType==null || documentNumber == null || documentNumber.trim().isEmpty()){
            throw new InvalidInputException("Document type and number cannot be empty.");
        }
        switch(documentType.toLowerCase()){
            case "aadhaar":
                if(documentNumber.length()!=12||!documentNumber.matches("\\d{12}")){
                    throw new InvalidInputException("aadhaar must be exactly 12 digits");
                }
                break;
            case "pan":
                if(documentNumber.length()!=10|| !documentNumber.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")){
                    throw new InvalidInputException("Invalid PAN format");
                }
                break;
            case "passport":
                if(documentNumber.length()<8){
                    throw new InvalidInputException("Invalid passport format");
                }
                break;
            default:
                throw new InvalidInputException("unsupported  document type. use aadhaar,pan or passport.");
        }
        KYC existingKyc=kycDao.findByUserId(userId);
        if(existingKyc!=null){
            throw new Exception("KYC already submitted. Current status"+existingKyc.getStatus());
        }
        KYC kyc = new KYC(userId,documentType.toUpperCase(),documentNumber,"PENDING");
        return kycDao.save(kyc);
    }

    public KYC getKYCStatus(int userId)throws Exception{
        return kycDao.findByUserId(userId);
    }

    public void approveKYC(int targetUserId) throws Exception {
        KYC kyc = kycDao.findByUserId(targetUserId);
        if(kyc==null){
            throw new Exception("KYC record not found for user ID: "+targetUserId);
        }
        if("APPROVED".equals(kyc.getStatus())){
            throw new Exception("KYC is already approved.");
        }
        kycDao.updateStatus(targetUserId,"APPROVED");
    }

    public List<KYC> getPendingKYCRequests()throws SQLException{
        return kycDao.findPending();
    }

    public void rejectKYC(int targetUserId)throws Exception{
        KYC kyc = kycDao.findByUserId(targetUserId);
        if(kyc==null){
            throw new Exception("KYC record not found for user ID: "+targetUserId);
        }
        if(!"PENDING".equals(kyc.getStatus())){
            throw new Exception("Only PENDING KYC requests can be rejected.");
        }
        kycDao.updateStatus(targetUserId,"REJECTED");
    }

}
