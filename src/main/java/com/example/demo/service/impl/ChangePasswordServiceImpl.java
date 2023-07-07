package com.example.demo.service.impl;

import com.example.demo.dao.entity.AccountEntity;
import com.example.demo.dao.repository.AccountRepository;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.ChangePasswordService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final AccountRepository accountRepository;

    public ChangePasswordServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public StatusCode changePassword(String id, String oldPassword, String newPassword) {
        try {
            String encodedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            AccountEntity accountEntity = accountRepository.getById(Long.parseLong(id));
            Boolean passwordMatches = BCrypt.checkpw(oldPassword, accountEntity.getPassword());

            if (passwordMatches) {
                accountEntity.setPassword(encodedPassword);
                accountRepository.save(accountEntity);
            } else {
                return StatusCode.InvalidOldPassword;
            }

            return StatusCode.ChangePasswordSuccess;
        } catch (Exception e) {
            return StatusCode.UnknownError;
        }

    }
}
