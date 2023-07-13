package com.example.demo.async;

import com.example.demo.dao.entity.CheckInStats;
import com.example.demo.dao.repository.CheckInStatsRepository;
import com.example.demo.dto.CheckInDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class CheckInTask {

    private static final Logger logger = LoggerFactory.getLogger(CheckInTask.class);

    @Qualifier("checkInExecutor")
    private final Executor checkInExecutor;

    private final CheckInStatsRepository checkInStatsRepository;

    public CheckInTask(Executor checkInExecutor, CheckInStatsRepository checkInStatsRepository) {
        this.checkInExecutor = checkInExecutor;
        this.checkInStatsRepository = checkInStatsRepository;
    }

    public void checkInTask(List<CheckInDto> list) {
        CompletableFuture<?>[] futures =  list.stream()
                .map(checkInDto -> CompletableFuture.runAsync(() -> calculateCheckInTime(checkInDto), checkInExecutor))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures)
                .thenRun(() -> logger.info("All done"));


    }

    private void calculateCheckInTime(CheckInDto checkInDto) {
        try {
            Duration timeDifference = Duration.between(checkInDto.getFirstCheckInTime(), checkInDto.getLastCheckInTime());
            CheckInStats stats = new CheckInStats(checkInDto.getAccount(), timeDifference.toMinutes());
            checkInStatsRepository.save(stats);
            logger.info("username " + checkInDto.getAccount().getUsername() + " done");
        } catch (Exception e) {
            logger.error("username " + checkInDto.getAccount().getUsername() + " error");
        }
    }
}
