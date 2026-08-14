package com.onion.metro.model;

import java.util.Map;

public record MetroResponse(
        String command,
        String selector,
        String settings,
        String data,
        Map<String, Object> dialogOptions
) {}
