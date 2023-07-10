package com.example.demo.service.impl;

import com.example.demo.dao.entity.AccountEntity;
import com.example.demo.dao.mapper.AccountMapper;
import com.example.demo.dto.AccountDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.LoginService;
import com.example.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginServiceImpl implements LoginService {

    private final AccountMapper accountMapper;

    public LoginServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public Map<String, Object> login(AccountDto accountDTO) {

        Optional<AccountEntity> accountEntity = accountMapper.queryByUsername(accountDTO.getUsername());
        if(accountEntity.isEmpty()) {
            return Map.of("statusCode", StatusCode.InvalidData);
        }

        boolean isPasswordMatch = BCrypt.checkpw(accountDTO.getPassword(), accountEntity.get().getPassword());

        Map<String, Object> map =new HashMap<String, Object>();
        if (isPasswordMatch) {
            String token = JwtUtil.generateToken(accountEntity.get().getId().toString());
            map.put("statusCode", StatusCode.LoginSuccess);
            map.put("token", token);
        } else {
            map.put("statusCode", StatusCode.InvalidData);
        }

        return map;
    }
}
