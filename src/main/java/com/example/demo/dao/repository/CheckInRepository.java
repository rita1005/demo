package com.example.demo.dao.repository;

import com.example.demo.dao.entity.CheckIn;
import com.example.demo.dto.CheckInDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    @Query("SELECT NEW com.example.demo.dto.CheckInDto(c.account, MIN(c.checkInTime), MAX(c.checkInTime)) " +
            "FROM CheckIn c WHERE c.checkInTime > :startDate AND c.checkInTime < :endDate " +
            "GROUP BY c.account")
    List<CheckInDto> findUserCheckInTimeInfo(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

}
