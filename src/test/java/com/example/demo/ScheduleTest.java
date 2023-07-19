package com.example.demo;

import com.example.demo.async.CheckInTask;
import com.example.demo.dao.entity.Account;
import com.example.demo.dao.entity.CheckInStats;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.dao.repository.CheckInStatsRepository;
import com.example.demo.dto.CheckInDto;
import com.example.demo.schedule.CheckInScheduler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@EnableScheduling
@SpringBootTest
public class ScheduleTest {
    @Autowired
    private CheckInScheduler checkInScheduler;
    @MockBean
    private CheckInRepository checkInRepository;
    @MockBean
    private CheckInStatsRepository checkInStatsRepository;
    @MockBean
    private CheckInTask checkInTask;

    @Test
    public void scheduleCheckInProcessing() {
        List<CheckInDto> checkInDtoList = List.of(
                new CheckInDto(new Account("Leo", "123"),
                        LocalDateTime.of(2023, 7, 19, 8, 30),
                        LocalDateTime.of(2023, 7, 19, 15, 00)),
                new CheckInDto(new Account("Lily", "123"),
                        LocalDateTime.of(2023, 7, 19, 9, 45),
                        LocalDateTime.of(2023, 7, 19, 17, 36)),
                new CheckInDto(new Account("Amy", "123"),
                        LocalDateTime.of(2023, 7, 19, 10, 23),
                        LocalDateTime.of(2023, 7, 19, 18, 20)));

        when(checkInRepository.findUserCheckInTimeInfo(any(), any())).thenReturn(checkInDtoList);
        when(checkInStatsRepository.save(any())).thenReturn(new CheckInStats());

        checkInScheduler.scheduleCheckInProcessing();

        verify(checkInRepository).findUserCheckInTimeInfo(any(), any());
        verify(checkInTask).checkInTask(checkInDtoList);


    }
}