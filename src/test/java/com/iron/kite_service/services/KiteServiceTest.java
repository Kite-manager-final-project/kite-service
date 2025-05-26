package com.iron.kite_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.kite_service.models.Kite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
class KiteServiceTest {

    @Autowired
    KiteService kiteService;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    //GET

    @Test
    @DisplayName("Obtener una cometa por ID")
    void findKiteById() throws Exception {
        int existingId = 1;

        mockMvc.perform(get("/api/kite/"+existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    @DisplayName("Buscar una cometa inexistente")
    void findNonExistingKite() throws Exception {

        mockMvc.perform(get("/api/kite/40")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La cometa que intentas buscar no existe"));
    }


    //POST

    @Test
    @Transactional
    @DisplayName("Guardar una cometa con un dueño existente")
    void saveKite() throws Exception {
        Kite kite = new Kite(25, "Sevilla", "tester");

        String kiteJSON = objectMapper.writeValueAsString(kite);

        mockMvc.perform(post("/api/kite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(kiteJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(kite.getLocation()))
                .andExpect(jsonPath("$.owner").value(kite.getOwner()))
                .andExpect(jsonPath("$.windRequired").value(kite.getWindRequired()));
    }

    @Test
    @DisplayName("Guardo la cometa con un dueño inexistente")
    void saveKiteWithUnexistingOwner() throws Exception {
        Kite kite = new Kite(25, "Cáceres", "fernan");

        String kiteJSON = objectMapper.writeValueAsString(kite);

        mockMvc.perform(post("/api/kite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(kiteJSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El dueño '" + kite.getOwner() + "' no existe."));
    }

    //PUT

    //PATCH

    //DELETE







}