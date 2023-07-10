package com.example.demo.service.impl;

import com.example.demo.dao.entity.Account;
import com.example.demo.dao.repository.AccountRepository;
import com.example.demo.dto.AccountDto;
import com.example.demo.enums.StatusCode;
import com.example.demo.service.RegisterService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final AccountRepository accountRepository;

    public RegisterServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public StatusCode register(AccountDto accountDTO) {
        if (accountRepository.findByUsername(accountDTO.getUsername()).isPresent()) {
            return StatusCode.ExistingAccount;
        }

        String encodedPassword = BCrypt.hashpw(accountDTO.getPassword(), BCrypt.gensalt());

        Account account = new Account(accountDTO.getUsername(), encodedPassword);
        accountRepository.save(account);

        return StatusCode.Created;
    }



}
