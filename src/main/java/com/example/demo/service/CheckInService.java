package com.example.demo.service;

import java.time.LocalDateTime;

public interface CheckInService {
    public void checkIn(Long userId, LocalDateTime time);

    public void errorCheckIn(Long userId, LocalDateTime time);

    public void secondErrorCheckIn(Long userId, LocalDateTime time);
}
