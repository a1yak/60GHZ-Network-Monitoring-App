package com.example.networkmonitoring.model;

import lombok.Data;

@Data
public class SpeedtestResult {
    private long last_changed;
    private String peer_mac;
    private boolean reverse;
    private String status;
    private double mbps;
}
