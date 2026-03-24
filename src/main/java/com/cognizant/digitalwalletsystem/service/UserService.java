package com.cognizant.digitalwalletsystem.service;
import com.cognizant.digitalwalletsystem.dao.NotificationDao;
import com.cognizant.digitalwalletsystem.dao.UserDao;
import com.cognizant.digitalwalletsystem.dao.WalletDao;
import com.cognizant.digitalwalletsystem.exception.AuthenticationException;
import com.cognizant.digitalwalletsystem.exception.DuplicateUserException;
import com.cognizant.digitalwalletsystem.exception.InvalidInputException;
import com.cognizant.digitalwalletsystem.exception.UserNotFoundException;
import com.cognizant.digitalwalletsystem.model.Notification;
import com.cognizant.digitalwalletsystem.model.User;
import com.cognizant.digitalwalletsystem.util.ValidationUtil;

import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;
    private final WalletDao walletDao;
    private final NotificationDao notificationDao;


    public UserService(UserDao userDao, WalletDao walletDao, NotificationDao notificationDao) {
        this.userDao = userDao;
        this.walletDao = walletDao;
        this.notificationDao = notificationDao;
    }

    public User registerUser(String name,String email,String phone,String password)throws InvalidInputException,DuplicateUserException,SQLException {
        if(name==null||name.trim().isEmpty()){
            throw new InvalidInputException("Name cannot be empty");
        }

        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePhone(phone);
        ValidationUtil.validatePassword(password);

        String passwordHash= String.valueOf(password.hashCode());
        User user = new User(name,email,phone,passwordHash);

        user=userDao.save(user);

        walletDao.createWallet(user.getId());

        notificationDao.save(new Notification(user.getId(), "Registration successful. Welcome to Digital Wallet"));
        return user;
    }

    public User loginUser(String email,String password)throws AuthenticationException{
        try {
            User user = userDao.findByEmail(email);
            String providedHash = String.valueOf(password.hashCode());

            if(!user.getPasswordHash().equals(providedHash)){
                throw new AuthenticationException("Invalid password");
            }
            return user;
        }catch(UserNotFoundException e){
            throw new AuthenticationException("Invalid email or password");
        }
    }

    public void changePassword(int userId,String newPassword)throws Exception{
        ValidationUtil.validatePassword(newPassword);
        User user = userDao.findById(userId);
        if(user==null){
            throw new Exception("User not found");
        }

        String newHash = String.valueOf(newPassword.hashCode());

        userDao.updatePassword(userId,newHash);
        notificationDao.save(new Notification(userId,"Password changed successfully"));
    }
}
