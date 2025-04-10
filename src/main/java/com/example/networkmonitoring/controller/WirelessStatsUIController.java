package com.example.networkmonitoring.controller;


import com.example.networkmonitoring.model.SpeedtestResult;
import com.example.networkmonitoring.service.NetworkService;
import com.example.networkmonitoring.service.SpeedTestService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    // This is your existing view endpoint
    @GetMapping("/stats")
    public String getStats(Model model) {
        ResponseEntity<String> responseEntity = networkService.fetchWirelessStats();
        String response = responseEntity.getBody();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Parse JSON into a Map or a custom object (if you have a defined structure)
                Map<String, Object> stats = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
                model.addAttribute("stats", stats); // Add the parsed object to the model

                // Convert the stats object back to a JSON string for display and JavaScript processing
                String jsonResponse = objectMapper.writeValueAsString(stats);
                model.addAttribute("jsonResponse", jsonResponse);

                // Add specific stats for direct access in Thymeleaf
                if (stats.containsKey("wireless") && stats.get("wireless") instanceof Map) {
                    Map<String, Object> wireless = (Map<String, Object>) stats.get("wireless");

                    // Extract peer data if available
                    if (wireless.containsKey("peers") && wireless.get("peers") instanceof List && !((List) wireless.get("peers")).isEmpty()) {
                        List<Map<String, Object>> peers = (List<Map<String, Object>>) wireless.get("peers");
                        if (!peers.isEmpty()) {
                            Map<String, Object> peer = peers.get(0);
                            model.addAttribute("peer", peer);
                        }
                    }

                    // Extract radio data if available
                    if (wireless.containsKey("radios") && wireless.get("radios") instanceof Map) {
                        Map<String, Object> radios = (Map<String, Object>) wireless.get("radios");
                        model.addAttribute("radios", radios);
                    }
                }
            } catch (Exception e) {
                model.addAttribute("error", "Error parsing JSON: " + e.getMessage());
                model.addAttribute("jsonResponse", "{}"); // Empty JSON in case of error
            }
        } else {
            model.addAttribute("error", "Error: " + response);
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