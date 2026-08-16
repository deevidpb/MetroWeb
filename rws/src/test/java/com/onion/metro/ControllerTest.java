package com.onion.metro;

import com.onion.metro.controller.MetroController;
import com.onion.metro.service.MetroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetroController.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetroService metroService;

    @Test
    void getArrival() throws Exception{
        when(metroService.getArrival("581")).thenReturn(TestData.getMockArrivals());
        mockMvc.perform(get("/api/metro/arrival/581")).andExpect(status().isOk());
        verify(metroService).getArrival("581");
    }

    @Test
    void getStations() throws Exception{
        when(metroService.getStations()).thenReturn(TestData.getMockStations());
        mockMvc.perform(get("/api/metro/stations")).andExpect(status().isOk());
        verify(metroService).getStations();
    }

    @Test
    void getLines() throws Exception{
        when(metroService.getLines()).thenReturn(TestData.getMockLines());
        mockMvc.perform(get("/api/metro/lines")).andExpect(status().isOk());
        verify(metroService).getLines();
    }
}
