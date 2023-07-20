package com.example.demo.schedule;

import com.example.demo.async.CheckInTask;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.dto.CheckInDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CheckInScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CheckInScheduler.class);

    private final CheckInRepository checkInRepository;

    private final CheckInTask checkInTask;

    public CheckInScheduler(CheckInRepository checkInRepository, CheckInTask checkInTask) {
        this.checkInRepository = checkInRepository;
        this.checkInTask = checkInTask;
    }

//    @Scheduled(cron = "0 0 23 * * ?")
//    @Scheduled(cron = "0 * * * * *")
    public void scheduleCheckInProcessing() {
        List<CheckInDto> list = checkInRepository
                .findUserCheckInTimeInfo(LocalDate.now().atStartOfDay(), LocalDateTime.now());
        logger.info("checkInDtoList size:" + list.size());
        list.forEach(c -> logger.info(c.getAccount().getUsername()));

        checkInTask.checkInTask(list);
    }
}
