package com.example.democrm.service;

import com.example.democrm.response.LoginResponse;

public interface AuthenService {
    LoginResponse authenticateUser(String userName, String password);

    //    void registerUser(String userName, String password, String email, Boolean isSuperAdmin);
    void registerUser(String userName, String password, String email, boolean isSuperAdmin);
}
