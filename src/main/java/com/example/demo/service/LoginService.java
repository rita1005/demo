package com.example.demo.service;

import com.example.demo.dto.AccountDto;

import java.util.Map;

public interface LoginService {
    public Map<String, Object> login(AccountDto accountDTO);
}
