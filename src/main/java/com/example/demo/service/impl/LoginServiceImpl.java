package com.example.demo.service.impl;

import com.example.demo.dao.entity.AccountEntity;
import com.example.demo.dao.mapper.AccountMapper;
import com.example.demo.dto.AccountDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.LoginService;
import com.example.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final AccountMapper accountMapper;

    public LoginServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public List login(AccountDto accountDTO) {
        try {

            Optional<AccountEntity> accountEntity = accountMapper.queryByUsername(accountDTO.getUsername());
            if(accountEntity.isEmpty()) {
                return List.of(StatusCode.InvalidData);
            }

            Boolean passwordMatches = BCrypt.checkpw(accountDTO.getPassword(), accountEntity.get().getPassword());

            List list =new ArrayList();
            if (passwordMatches) {
                String token = JwtUtil.generateToken(accountEntity.get().getId().toString());
                list.add(StatusCode.LoginSuccess);
                list.add(token);
            } else {
                list.add(StatusCode.InvalidData);
            }

            return list;
        } catch (Exception e) {
            return List.of(StatusCode.UnknownError);
        }
    }
}
