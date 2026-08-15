package com.onion.metro.model;

import java.util.List;

public record Station(
   String id,
   String name,
   List<String> lines,
   boolean accessible
) {}
