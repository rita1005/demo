package com.example.demo;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.async.CheckInTask;
import com.example.demo.controller.AccountController;
import com.example.demo.dao.entity.Account;
import com.example.demo.dao.entity.CheckInStats;
import com.example.demo.dao.repository.CheckInStatsRepository;
import com.example.demo.dto.CheckInDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    @Autowired
    private CheckInTask checkInTask;
    @MockBean
    private CheckInStatsRepository checkInStatsRepository;

    private ListAppender listAppender;

    @Before
    public void init() {
        Logger logger = (Logger) LoggerFactory.getLogger(CheckInTask.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }
    @Test
    public void CheckInTaskTest() {
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

        when(checkInStatsRepository.save(any())).thenReturn(new CheckInStats());

        checkInTask.checkInTask(checkInDtoList);

        verify(checkInStatsRepository, times(3)).save(any());

        List<ILoggingEvent> loggingEvents = listAppender.list;
    }
}
