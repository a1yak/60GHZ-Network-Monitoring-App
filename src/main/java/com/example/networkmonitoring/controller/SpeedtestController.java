//package com.example.networkmonitoring.controller;
//
//import com.example.networkmonitoring.service.SpeedTestService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//public class SpeedtestController {
//
//    @Autowired
//    private SpeedTestService speedtestService;
//
//    @GetMapping("/speedtest")
//    public String getSpeedtestResults(Model model) {
//
//        String result = speedtestService.fetchSpeedtestResults().getBody();
//
//        if (result != null) {
//            model.addAttribute("speedtest", result);
//        } else {
//            model.addAttribute("error", "Failed to fetch speedtest results.");
//        }
//
//        return "speedtest";
//    }
//
//    @PostMapping("/speedtest/run")
//    public String runSpeedtest(Model model) {
//
//        String result = speedtestService.runSpeedtest().getBody();
//
//        if (result != null) {
//            model.addAttribute("speedtest", result);
//        } else {
//            model.addAttribute("error", "Failed to run speedtest.");
//        }
//
//        return "speedtest";
//    }
//}
