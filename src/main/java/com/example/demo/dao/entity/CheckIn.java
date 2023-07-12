package com.example.demo.dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "check_in_time", columnDefinition = "INT NOT NULL")
    private LocalDateTime checkInTime;

    public CheckIn() {
    }

    public CheckIn(Account account, LocalDateTime checkInTime) {
        this.account = account;
        this.checkInTime = checkInTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}
