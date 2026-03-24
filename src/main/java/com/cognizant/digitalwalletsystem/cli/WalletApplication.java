package com.cognizant.digitalwalletsystem.cli;
import com.cognizant.digitalwalletsystem.dao.UserDao;
import com.cognizant.digitalwalletsystem.dao.UserDaoImpl;
import com.cognizant.digitalwalletsystem.factory.ServiceFactory;
import com.cognizant.digitalwalletsystem.model.*;
import com.cognizant.digitalwalletsystem.service.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class WalletApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final ServiceFactory factory = ServiceFactory.getInstance();

    private final UserService userService=factory.getUserService();
    private final WalletService walletService=factory.getWalletService();
    private final TransactionService transactionService=factory.getTransactionService();
    private final KYCService kycService=factory.getKycService();
    private final NotificationService notificationService = factory.getNotificationService();

    private final UserDao userDao = new UserDaoImpl();
    private User loggedInUser=null;

    public void start(){
        System.out.println("Welcome to Digital Wallet System");
        while(true) {
            try{
                if(loggedInUser==null){
                    showMainMenu();
                }else {
                    showUserMenu();
                }
            }catch(Exception e){
                System.out.println("Error "+e.getMessage());
            }
        }
    }

    private void showMainMenu(){
        System.out.println("\n1.Register");
        System.out.println("2.Login");
        System.out.println("3.Admin Panel");
        System.out.println("4.Exit");
        System.out.println("Choose an option");

        String choice = scanner.nextLine();
        switch(choice){
            case "1":handleRegistration();
            break;

            case "2":handleLogin();
                break;
            case "3":handleAdminPanel();
                break;
            case "4":
                System.out.println("Thank you for using our wallet");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option please try again");
        }

    }

    private void showUserMenu(){
        System.out.println("\n Dashboard [User:"+loggedInUser.getName()+"]");
        System.out.println("1.Deposit Money");
        System.out.println("2.check balance");
        System.out.println("3.Transfer Money");
        System.out.println("4.view Transactions");
        System.out.println("5.Submit KYC");
        System.out.println("6.view KYC Status");
        System.out.println("7.change password");
        System.out.println("8.view Notifications");
        System.out.println("9.Log Out");

        System.out.println("Choose an Option:");
        String choice = scanner.nextLine();
        try{
            switch(choice){
                case "1":handleDeposit();
                break;
                case "2":handleCheckBalance();
                    break;
                case "3":handleTransfer();
                    break;
                case "4":handleViewTransactions();
                    break;
                case "5":handleSubmitKYC();
                    break;
                case "6":handleViewKYCStatus();
                    break;
                case "7":handleChangePassword();
                    break;
                case "8":handleViewNotifications();
                    break;
                case "9":
                    loggedInUser=null;
                    System.out.println("Logged Out Successfully");
                    break;
                default:
                    System.out.println("Invalid option please try again");
            }
        }catch(Exception e){
            System.out.println("Error:"+e.getMessage());
        }
    }

    private void handleRegistration(){
        System.out.println("User Registration");
        System.out.println("Name:");
        String name=scanner.nextLine();
        System.out.println("Email:");
        String email=scanner.nextLine();
        System.out.println("Phone(10 digits):");
        String phone=scanner.nextLine();
        System.out.println("Password:");
        String password=scanner.nextLine();

        try{
            User user = userService.registerUser(name,email,phone,password);
            System.out.println("Registration successful wallet created with 0 balance for user ID:"+user.getId());
        }catch(Exception e){
            System.out.println("Registration failed:"+e.getMessage());
        }
    }

    private void handleLogin(){
        System.out.println("User Login");
        System.out.println("Email:");
        String email=scanner.nextLine();
        System.out.println("Password:");
        String password=scanner.nextLine();

        try{
            loggedInUser=userService.loginUser(email,password);
            System.out.println("Login successful. Welcome back"+loggedInUser.getName());
        }catch(Exception e){
            System.out.println("Login failed:"+e.getMessage());
        }
    }

    private void handleDeposit() throws Exception{
        System.out.println("Enter amount to deposit:");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        walletService.deposit(loggedInUser.getId(),amount);
        System.out.println("Successfully deposited "+amount+" to your wallet");
    }

    private void handleCheckBalance() throws Exception {
        Wallet wallet=walletService.getWalletByUserId(loggedInUser.getId());
        if(wallet!=null){
            System.out.println("Current wallet balance:"+wallet.getBalance());
        }else {
            System.out.println("Wallet not found");
        }
    }

    private void handleTransfer() throws Exception {
        System.out.println("Enter receiver email or phone");
        String receiver = scanner.nextLine();
        System.out.println("Enter amount to transfer");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        walletService.transfer(loggedInUser.getId(),receiver,amount,userDao);
        System.out.println("Successfully transferred "+amount+" to "+receiver);
    }

    private void handleViewTransactions() throws Exception{
        Wallet wallet = walletService.getWalletByUserId(loggedInUser.getId());
        if(wallet==null){
            System.out.println("Wallet not found");
            return;
        }
        List<Transaction> transactions = transactionService.getTransactionsByWalletId(wallet.getId());
        if(transactions.isEmpty()){
            System.out.println("No transactions found");
        }else {
            System.out.println("Transaction history");
            System.out.printf("%-10s|%-15s|%-10s|%-25s|%-20s%n","Tnx ID","Type","Amount","Description","Date");
            for(Transaction t:transactions){
                System.out.printf("%-10d|%-15s|%-10s|%-25s|%-20s%n",t.getId(),t.getType(),t.getAmount(),t.getDescription(),t.getCreatedAt());
            }
        }
    }

    private void handleSubmitKYC(){
        System.out.println("Submit KYC");
        System.out.println("Supported documents:AADHAAR,PAN,PASSPORT");
        System.out.println("Enter the document type:");
        String docType= scanner.nextLine();
        System.out.println("Enter the document number:");
        String docNum= scanner.nextLine();

        try{
            KYC kyc = kycService.submitKYC(loggedInUser.getId(), docType,docNum);
            System.out.println("KYC submitted successfully.Waiting for admin approval"+"current status is:"+kyc.getStatus());
        }catch(Exception e){
            System.out.println("KYC Submission failed"+e.getMessage());
        }
    }

    private void handleViewKYCStatus()throws Exception{
        KYC kyc = kycService.getKYCStatus(loggedInUser.getId());
        if(kyc==null){
            System.out.println("No KYC record found.please submit your KYC first");
        }else {
            System.out.println("KYC Status:"+kyc.getStatus()+"(Doc:"+kyc.getDocumentType()+"-"+kyc.getDocumentNumber()+")");
        }
    }
    private void handleChangePassword(){
        System.out.println("Enter the new password:");
        String newPassword = scanner.nextLine();
        try {
            userService.changePassword(loggedInUser.getId(),newPassword);
            System.out.println("Password changes successfully");
        }catch(Exception e){
            System.out.println("Failed to change password:"+e.getMessage());
        }
    }

    private void handleAdminPanel(){
        System.out.println("Admin panel");
        System.out.println("Enter admin password:");
        String pass=scanner.nextLine();
        if(!"admin".equals(pass)){
            System.out.println("Invalid Admin Password");
            return;
        }
        while(true) {
            System.out.println("===Admin Panel===");
            System.out.println("1.view pending KYC requests");
            System.out.println("2.Approve KYC");
            System.out.println("3.Reject kyc");
            System.out.println("4.Back to Main menu");
            System.out.println("Choose an option:");
            String choice = scanner.nextLine();

            try{
                switch(choice){
                    case "1":List<KYC> pending = kycService.getPendingKYCRequests();
                    if(pending.isEmpty()){
                        System.out.println("No Pending KYC requests");
                    }else {
                        System.out.println("Pending KYC");
                        for(KYC kyc:pending){
                            System.out.println("User ID:"+kyc.getUserId()+"|Doc:"+kyc.getDocumentType()+"|Num:"+kyc.getDocumentNumber());
                        }
                    }
                    break;
                    case "2":
                        System.out.println("Enter User ID to approach KYC");
                        int approvedId=Integer.parseInt(scanner.nextLine());
                        kycService.approveKYC(approvedId);
                        System.out.println("KYC for User ID"+approvedId+" APPROVED");
                        break;
                    case "3":
                        System.out.println("Enter User ID to reject KYC");
                        int rejectedId=Integer.parseInt(scanner.nextLine());
                        kycService.rejectKYC(rejectedId);
                        System.out.println("KYC for User ID"+rejectedId+" REJECTED");
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            }catch(Exception e){
                System.out.println("Admin action failed "+e.getMessage());
            }
        }
    }

    private void handleViewNotifications()throws Exception{
        System.out.println("Your Notifications");
        List<Notification> messages = notificationService.getNotificationForUser(loggedInUser.getId());
        if(messages.isEmpty()){
            System.out.println("You have no notifications");
            return;
        }

        for(Notification msg:messages){
            String readStatus = msg.isRead()?"[Read]":"[UnRead]";
            System.out.println(readStatus+" "+msg.getCreatedAt()+"-"+msg.getMessage());
        }
        notificationService.markAsRead(loggedInUser.getId());
    }

}
