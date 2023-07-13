package com.example.demo.component;

import com.example.demo.service.CheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    private final CheckInService checkInService;

    public CommonExceptionHandler(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.checkIn(id, currentTime);
        logger.error("Account id:" + id +" error check-in api test");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error check-in api test completed");
    }
}
