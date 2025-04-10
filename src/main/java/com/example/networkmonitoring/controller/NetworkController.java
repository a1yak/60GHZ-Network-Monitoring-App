package com.example.networkmonitoring.controller;

import com.example.networkmonitoring.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NetworkController {

    private final NetworkService networkService;


    @GetMapping("/wireless-stats")
    public ResponseEntity<String> getWirelessStats() {
        return networkService.fetchWirelessStats();
    }
}
