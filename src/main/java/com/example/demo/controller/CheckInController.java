package com.example.demo.controller;

import com.example.demo.service.CheckInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api(tags = "CHECK-IN API")
@RestController
@RequestMapping("/api")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @ApiOperation("check-in")
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.checkIn(id, currentTime);
        return ResponseEntity.status(HttpStatus.OK).body("Check-in successful");
    }

    @ApiOperation("error check-in")
    @PostMapping("/errorCheck-in")
    public ResponseEntity<String> errorCheckIn() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.errorCheckIn(id, currentTime);
        return ResponseEntity.status(HttpStatus.OK).body("Check-in successful");
    }

    @ApiOperation("second error check-in")
    @PostMapping("/secondErrorCheck-in")
    public ResponseEntity<String> secondErrorCheckIn() {
        Long id = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.secondErrorCheckIn(id, currentTime);
        return ResponseEntity.status(HttpStatus.OK).body("Check-in successful");
    }
}
