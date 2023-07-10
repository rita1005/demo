package com.example.demo.controller;

import com.example.demo.dto.vo.CommonResponse;
import com.example.demo.service.CheckInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public ResponseEntity checkIn() {
        Long id = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime currentTime = LocalDateTime.now();
        checkInService.checkIn(id, currentTime);
        return (ResponseEntity) ResponseEntity.status(HttpStatus.OK);
    }
}
