package com.example.demo.dao.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private Long userId;

    @Column(name = "check_in_time", columnDefinition = "INT NOT NULL")
    private LocalDateTime checkInTime;

    public CheckIn() {
    }

    public CheckIn(Long userId, LocalDateTime checkInTime) {
        this.userId = userId;
        this.checkInTime = checkInTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}
