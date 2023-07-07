package com.example.demo.service;

import com.example.demo.enums.StatusCode;


public interface ChangePasswordService {
    public StatusCode changePassword(String id, String oldPassword, String newPassword);
}
