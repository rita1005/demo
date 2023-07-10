package com.example.demo.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "check_in_stats")
public class CheckInStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private Long userId;

    @Column(name = "time_difference", columnDefinition = "INT NOT NULL")
    private Long timeDifference;

    public CheckInStats() {
    }

    public CheckInStats(Long userId, Long timeDifference) {
        this.userId = userId;
        this.timeDifference = timeDifference;
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

    public Long getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Long timeDifference) {
        this.timeDifference = timeDifference;
    }
}
