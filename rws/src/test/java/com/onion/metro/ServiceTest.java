package com.onion.metro;

import com.onion.metro.client.MetroApiClient;
import com.onion.metro.model.Arrival;
import com.onion.metro.model.MetroResponse;
import com.onion.metro.model.Station;
import com.onion.metro.service.MetroService;
import com.onion.metro.utils.TimesParse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private MetroApiClient client;

    @InjectMocks
    private MetroService service;

    @Test
    void getArrival(){
        when(client.getTimes("581")).thenReturn(new MetroResponse("",
                "", "", TestData.getMockHtml(), Map.of()));

        Arrival test1 = new Arrival("Línea 7", "Pitis", "2 min", "normal");
        Arrival test2 = new Arrival("Línea 7", "Hospital del Henares", "A la espera de previsión", "unavailable");

        List<Arrival> response = service.getArrival("581");
        assertNotNull(response);
        assertEquals(3, response.size());
        Arrival arrival1 =  response.get(0);
        Arrival arrival2 = response.get(1);
        assertEquals(test1, arrival1);
        assertEquals(test2, arrival2);
    }

    @Test
    void getArrival2(){
        when(client.getTimes("581")).thenReturn(new MetroResponse("",
                "", "", TestData.getMockHtml2(), Map.of()));

        Arrival test1 = new Arrival("Ramal", "", "A la espera de previsión", "unavailable");
        Arrival test2 = new Arrival("Línea 6", "Lucero - Andén 1", "A la espera de previsión", "unavailable");
        Arrival test3 = new Arrival("Línea 6", "Laguna - Andén 2", "5 min", "normal");


        List<Arrival> response = service.getArrival("581");
        assertNotNull(response);
        assertEquals(5, response.size());
        Arrival arrival1 =  response.get(0);
        Arrival arrival2 = response.get(1);
        Arrival arrival3 = response.get(2);
        assertEquals(test1, arrival1);
        assertEquals(test2, arrival2);
        assertEquals(test3, arrival3);
    }

    @Test
    void getArrivalNull(){
        when(client.getTimes("581")).thenReturn(null);

        List<Arrival> response = service.getArrival("581");
        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void getStations(){
        List<Station> stations = service.getStations();

        Station test = new Station("581", "Pinar de Chamartín", List.of("Línea 1",
                "Línea 4",
                "ML1"), true);

        assertNotNull(stations);
        assertEquals(249, stations.size());
        Station station = stations.getFirst();
        assertEquals(test, station);
    }

    @Test
    void getLines(){
        Map<String, List<String>> lines = service.getLines();

        List<String> test = List.of("581", "438", "461", "586", "637", "629",
                "490", "418", "475", "605", "514", "447", "633", "501", "627", "630", "420",
                "431", "432", "553", "568", "595", "561", "589", "449", "416", "555", "625",
                "646", "471", "523", "535", "639");

        assertNotNull(lines);
        assertEquals(14, lines.size());
        List<String> line = lines.get("Línea 1");
        assertEquals(test, line);

    }

    @ParameterizedTest(name = "changeLine(\"{0}\") debería devolver \"{1}\"")
    @CsvSource({
            "linea-1, Línea 1",
            "linea-2, Línea 2",
            "linea-3, Línea 3",
            "linea-4, Línea 4",
            "linea-5, Línea 5",
            "linea-6-circular, Línea 6",
            "linea-7, Línea 7",
            "linea-8, Línea 8",
            "linea-9, Línea 9",
            "linea-10, Línea 10",
            "linea-11, Línea 11",
            "linea-12-metrosur, Línea 12",
            "ramal, Ramal",
            "ml1, ML1",
            "linea-desconocida, linea-desconocida", // Prueba del caso 'default'
            "'' , ''"                               // Entrada vacía
    })
    void testChangeLine(String input, String expected) {
        String actual = TimesParse.changeLine(input);
        assertEquals(expected, actual);
    }
}
