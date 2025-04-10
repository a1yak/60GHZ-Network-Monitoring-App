package com.example.networkmonitoring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SpeedTestService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SPEEDTEST_URL = "http://192.168.0.103/cgi.lua/apiv1/speedtest";
    private static final String API_TOKEN ="BBHR36DHAAAAAABFXRSDY4IYQ76NJWHGE6UTIGADBJHQ%3D%3D%3D%3D";


    public ResponseEntity<String> fetchSpeedtestResults() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "api_token=" + API_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(SPEEDTEST_URL, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> runSpeedtest() {
        String jsonRequest = "{\"reverse\": false }"; // Body for POST request

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "api_token=" + API_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        try {
            return restTemplate.exchange(SPEEDTEST_URL, HttpMethod.POST, entity, String.class);
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting speedtest: Connection reset or server unreachable");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting speedtest: " + e.getMessage());
        }
    }
}
