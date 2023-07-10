package com.example.demo.service.impl;

import com.example.demo.dao.entity.Account;
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

        String encodedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        Account account = accountRepository.getById(Long.parseLong(id));
        boolean isPasswordMatch = BCrypt.checkpw(oldPassword, account.getPassword());

        if (isPasswordMatch) {
            account.setPassword(encodedPassword);
            accountRepository.save(account);
        } else {
            return StatusCode.InvalidOldPassword;
        }

        return StatusCode.ChangePasswordSuccess;


    }
}
