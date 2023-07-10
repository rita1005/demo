package com.example.demo.service.impl;

import com.example.demo.dao.entity.CheckIn;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.service.CheckInService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;

    public CheckInServiceImpl(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    @Override
    public void checkIn(Long userId, LocalDateTime time) {
        CheckIn checkIn = new CheckIn(userId, time);
        checkInRepository.save(checkIn);
    }
}
