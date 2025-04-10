package com.example.networkmonitoring.controller;


import com.example.networkmonitoring.model.SpeedtestResult;
import com.example.networkmonitoring.service.NetworkService;
import com.example.networkmonitoring.service.SpeedTestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WirelessStatsUIController {



    private final NetworkService networkService;

    private final SpeedTestService speedTestService;
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/stats")
    public String getStats(Model model) {
        ResponseEntity<String> responseEntity = networkService.fetchWirelessStats();
        String response = responseEntity.getBody();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Parse JSON into a Map or a custom object (if you have a defined structure)
                Map<String, Object> stats = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
                model.addAttribute("stats", stats); // Add the parsed object to the model

                // Convert the stats object back to a JSON string for display
                String jsonResponse = objectMapper.writeValueAsString(stats);
                model.addAttribute("jsonResponse", jsonResponse);
            } catch (Exception e) {
                model.addAttribute("stats", "Error parsing JSON: " + e.getMessage());
                model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
            }
        } else {
            model.addAttribute("stats", "Error: " + response);
            model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
        }
        return "stats";
    }

    @GetMapping("/speedtest")
    public String getSpeedPage(Model model) {
        ResponseEntity<String> responseEntity = speedTestService.fetchSpeedtestResults();
        String response = responseEntity.getBody();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Parse JSON into SpeedTestResult object
                SpeedtestResult speedTestResult = objectMapper.readValue(response, SpeedtestResult.class);
                model.addAttribute("speedtest", speedTestResult); // Add the object to the model

                // Convert SpeedTestResult to JSON string for display
                String jsonResponse = objectMapper.writeValueAsString(speedTestResult);
                model.addAttribute("jsonResponse", jsonResponse);
            } catch (Exception e) {
                model.addAttribute("speedtest", "Error parsing JSON: " + e.getMessage());
                model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
            }
        } else {
            model.addAttribute("speedtest", "Error: " + response);
            model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
        }
        return "speedtest";
    }

    @PostMapping("/speedtest/run")
    public String runSpeedtest(Model model) {
        ResponseEntity<String> responseEntity = speedTestService.runSpeedtest();
        String response = responseEntity.getBody();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Parse JSON into SpeedTestResult object
                SpeedtestResult speedTestResult = objectMapper.readValue(response, SpeedtestResult.class);
                model.addAttribute("speedtest", speedTestResult); // Add the object to the model

                // Convert SpeedTestResult to JSON string for display
                String jsonResponse = objectMapper.writeValueAsString(speedTestResult);
                model.addAttribute("jsonResponse", jsonResponse);
            } catch (Exception e) {
                model.addAttribute("speedtest", "Error parsing JSON: " + e.getMessage());
                model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
            }
        } else {
            model.addAttribute("speedtest", "Error: " + response);
            model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
        }
        return "speedtest";
    }

}