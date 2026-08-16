package com.onion.metro.controller;

import com.onion.metro.model.Arrival;
import com.onion.metro.model.Station;
import com.onion.metro.service.MetroService;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metro")
public class MetroController {
    private final MetroService metroService;

    MetroController(MetroService metroService) {
        this.metroService = metroService;
    }

    @GetMapping("/arrival/{id}")
    public List<Arrival> getArrival(@PathVariable @Positive String id){
        return metroService.getArrival(id);
    }

    @GetMapping("/stations")
    public List<Station> getStations(){
        return metroService.getStations();
    }

    @GetMapping("/lines")
    public Map<String, List<String>> getLines(){
        return metroService.getLines();
    }
}
