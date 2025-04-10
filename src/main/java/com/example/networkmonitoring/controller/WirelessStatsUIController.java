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

    @PostMapping("/speedtest/run")
    public String runSpeedtest() {
        // Just initiate the speedtest, don't fetch results yet
        ResponseEntity<String> responseEntity = speedTestService.runSpeedtest();

        // Redirect to speedtest page where JavaScript will handle polling for results
        return "redirect:/speedtest";
    }


    @GetMapping("/speedtest/results")
    @ResponseBody
    public ResponseEntity<SpeedtestResult> getSpeedtestResults() {
        ResponseEntity<String> responseEntity = speedTestService.fetchSpeedtestResults();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Parse JSON into SpeedTestResult object
                SpeedtestResult speedTestResult = objectMapper.readValue(responseEntity.getBody(), SpeedtestResult.class);
                return ResponseEntity.ok(speedTestResult);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).build();
        }
    }

    @GetMapping("/speedtest")
    public String showSpeedtestPage(Model model) {
        // Get initial results to display
        ResponseEntity<String> responseEntity = speedTestService.fetchSpeedtestResults();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                // Add the JSON string to the model for initial rendering
                model.addAttribute("jsonResponse", responseEntity.getBody());
            } catch (Exception e) {
                // If there's an error, just provide an empty JSON object
                model.addAttribute("jsonResponse", "{}");
            }
        } else {
            model.addAttribute("jsonResponse", "{}");
        }

        // Return the speedtest.html template
        return "speedtest";
    }
}