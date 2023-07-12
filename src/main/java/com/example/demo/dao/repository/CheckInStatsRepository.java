package com.example.demo.dao.repository;

import com.example.demo.dao.entity.CheckInStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInStatsRepository extends JpaRepository<CheckInStats, Long>{
}
