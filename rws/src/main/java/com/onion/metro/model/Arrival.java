package com.onion.metro.model;

public record Arrival(
        String line,
        String destination,
        String time,
        String status
) {}
