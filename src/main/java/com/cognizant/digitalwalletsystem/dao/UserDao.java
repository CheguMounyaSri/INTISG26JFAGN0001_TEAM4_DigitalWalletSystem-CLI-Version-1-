package com.cognizant.digitalwalletsystem.dao;
import com.cognizant.digitalwalletsystem.model.User;
import com.cognizant.digitalwalletsystem.exception.DuplicateUserException;
import com.cognizant.digitalwalletsystem.exception.UserNotFoundException;

public interface UserDao {
    User save(User user)throws DuplicateUserException;
    User findByEmail(String email)throws UserNotFoundException;
    User findByPhone(String phone)throws UserNotFoundException;
    User findById(int id)throws UserNotFoundException;
    void updatePassword(int userId,String newPasswordHash) throws Exception;
}
