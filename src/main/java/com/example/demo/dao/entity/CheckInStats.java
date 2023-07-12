package com.example.demo.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "check_in_stats")
public class CheckInStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "time_difference", columnDefinition = "INT NOT NULL")
    private Long timeDifference;

    public CheckInStats() {
    }

    public CheckInStats(Account account, Long timeDifference) {
        this.account = account;
        this.timeDifference = timeDifference;
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

    public Long getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Long timeDifference) {
        this.timeDifference = timeDifference;
    }
}
