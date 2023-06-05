package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;

import java.util.List;

public interface UserDAO {
    boolean isUsernameUnique(String username) throws ServletException;

    void addUser(User user) throws ServletException;

    User authenticate(String username, String password) throws ServletException;

    int getID(String customerUsername) throws ServletException;

    User checkUserDetails(String username, String email, String phone) throws ServletException;

    boolean updatePassword(int user, String newPassword) throws ServletException;

    boolean changeRole(String customerId, String agent);

    List<User> getAllAgents();

    List<User> getOnlyCustomers();

    User getUserById(String customerId);

    boolean updateUser(User user);

    List<User> getNotLinkedCustomers();

    List<User> getLinkedCustomers(String userId);
}
