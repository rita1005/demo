package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.StatusCode;

public interface RegisterService {
    public StatusCode register(AccountDto accountDTO);
}
