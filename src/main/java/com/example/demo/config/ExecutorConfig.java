package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {

    @Bean(name = "checkInExecutor")
    public Executor checkInExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(8);
        executor.setCorePoolSize(8);
        executor.setThreadNamePrefix("checkInExecutor-");
        executor.initialize();
        return executor;
    }
}
