package com.example.demo.async;

import com.example.demo.dao.entity.CheckInStats;
import com.example.demo.dao.repository.CheckInStatsRepository;
import com.example.demo.dto.CheckInDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CheckInTask {

    private final CheckInStatsRepository checkInStatsRepository;

    public CheckInTask(CheckInStatsRepository checkInStatsRepository) {
        this.checkInStatsRepository = checkInStatsRepository;
    }

    @Async("checkInExecutor")
    public void checkInTask(List<CheckInDto> list) {
        CompletableFuture<?>[] futures =  list.stream()
                .map(checkInDto -> CompletableFuture.runAsync(() -> calculateCheckInTime(checkInDto)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures)
                .thenRun(() -> System.out.println("All done"));


    }

    private void calculateCheckInTime(CheckInDto checkInDto) {
        Duration timeDifference = Duration.between(checkInDto.getFirstCheckInTime(), checkInDto.getLastCheckInTime());
        CheckInStats stats = new CheckInStats(checkInDto.getAccount(), timeDifference.toMinutes());
        checkInStatsRepository.save(stats);
    }
}
