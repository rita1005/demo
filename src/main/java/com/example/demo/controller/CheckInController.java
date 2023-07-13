package com.example.demo.controller;

import com.example.demo.service.CheckInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.checkIn(id, currentTime);
        return ResponseEntity.status(HttpStatus.OK).body("Check-in successful");
    }

    @PostMapping("/errorCheck-in")
    public ResponseEntity<String> errorCheckIn() {
        throw new RuntimeException();
    }
}
