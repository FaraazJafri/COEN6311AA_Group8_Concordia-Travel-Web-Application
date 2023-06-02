package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;

public interface UserDAO {
    boolean isUsernameUnique(String username);
    void addUser(User user);
    User authenticate(String username, String password) throws ServletException;
    int getID(String customerUsername);
}
