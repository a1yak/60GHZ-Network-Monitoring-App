package com.example.networkmonitoring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class NetworkService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "http://192.168.0.104/cgi.lua/apiv1/stats?type=wireless";
    private static final String API_TOKEN = "BA44D53HAAAAAAAUJ477GCHPUISM4ACVDP5D2TUCGLIA%3D%3D%3D%3D";

    public ResponseEntity<String> fetchWirelessStats() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "api_token=" + API_TOKEN);  // Use Cookie header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        return response;
    }
}
