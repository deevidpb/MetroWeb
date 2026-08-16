package com.onion.metro.service;

import com.onion.metro.client.MetroApiClient;
import com.onion.metro.model.Arrival;
import com.onion.metro.model.MetroResponse;
import com.onion.metro.model.Station;
import com.onion.metro.utils.TimesParse;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class MetroService {

    private final MetroApiClient metroClient;
    private final ObjectMapper mapper;

    public MetroService(MetroApiClient metroClient) {
        this.metroClient = metroClient;
        this.mapper = new ObjectMapper();
    }

    public List<Arrival> getArrival(String stationId){
        MetroResponse response = metroClient.getTimes(stationId);
        if (response == null){
            return List.of();
        }
        return TimesParse.parseArrivals(response.data());
    }

    public List<Station> getStations(){
        return mapper.readValue(
                getClass().getResourceAsStream("/stations.json"),
                new TypeReference<>() {
                }
        );
    }

    public Map<String, List<String>> getLines(){
        return mapper.readValue(
                getClass().getResourceAsStream("/lines.json"),
                new TypeReference<>() {
                }
        );
    }
}
