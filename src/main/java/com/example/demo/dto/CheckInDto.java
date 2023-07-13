package com.example.demo.dto;

import com.example.demo.dao.entity.Account;

import java.time.LocalDateTime;

public class CheckInDto {

    private Account account;
    private LocalDateTime firstCheckInTime;
    private LocalDateTime lastCheckInTime;

    public CheckInDto(Account account, LocalDateTime firstCheckInTime, LocalDateTime lastCheckInTime) {
        this.account = account;
        this.firstCheckInTime = firstCheckInTime;
        this.lastCheckInTime = lastCheckInTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getFirstCheckInTime() {
        return firstCheckInTime;
    }

    public void setFirstCheckInTime(LocalDateTime firstCheckInTime) {
        this.firstCheckInTime = firstCheckInTime;
    }

    public LocalDateTime getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setLastCheckInTime(LocalDateTime lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }
}
