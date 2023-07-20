package com.example.demo.service.impl;

import com.example.demo.dao.entity.CheckIn;
import com.example.demo.dao.repository.AccountRepository;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.service.CheckInService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;

    private final AccountRepository accountRepository;

    public CheckInServiceImpl(CheckInRepository checkInRepository, AccountRepository accountRepository) {
        this.checkInRepository = checkInRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void checkIn(Long userId, LocalDateTime time) {
        CheckIn checkIn = new CheckIn(accountRepository.getById(userId), time);
        checkInRepository.save(checkIn);
    }

    @Override
    public void errorCheckIn(Long userId, LocalDateTime time) {
        CheckIn checkIn = new CheckIn(accountRepository.getById(userId), time);
        checkInRepository.save(checkIn);
        throw new RuntimeException("error check-in without @Transactional");
    }

    @Override
    @Transactional
    public void secondErrorCheckIn(Long userId, LocalDateTime time) {
        CheckIn checkIn = new CheckIn(accountRepository.getById(userId), time);
        checkInRepository.save(checkIn);
        throw new RuntimeException("error check-in with @Transactional");
    }

}
