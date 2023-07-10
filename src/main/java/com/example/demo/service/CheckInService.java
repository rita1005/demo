package com.example.demo.service;

import java.time.LocalDateTime;

public interface CheckInService {
    public void checkIn(Long userId, LocalDateTime time);
}
