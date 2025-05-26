package com.iron.kite_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.kite_service.dtos.KiteUpdatedLocationDTO;
import com.iron.kite_service.dtos.KiteUpdatedWindDTO;
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
        Kite request = new Kite(25, "Sevilla", "tester");

        String kiteJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/kite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(kiteJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(request.getLocation()))
                .andExpect(jsonPath("$.owner").value(request.getOwner()))
                .andExpect(jsonPath("$.windRequired").value(request.getWindRequired()));
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

    @Test
    @DisplayName("Modifico una cometa completa")
    @Transactional
    void updateKite() throws Exception {

        Kite request = new Kite(23, "Albacete", "auronplay");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/kite/17")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(request.getLocation()))
                .andExpect(jsonPath("$.owner").value(request.getOwner()))
                .andExpect(jsonPath("$.windRequired").value(request.getWindRequired()));
    }


    @Test
    @DisplayName("Intento cambiarle el sueño a una cometa existente")
    void changeOwnerKite() throws Exception {

        Kite request = new Kite(23, "Albacete", "hombre_de_la_rae");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/kite/17")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("No le puedes cambiar el dueño a una cometa, " +
                        "tienes que pasarle el mismo dueño"));


    }

    @Test
    @DisplayName("Intento modificar una cometa completamente con un id no existente")
    void updateUnExistingKite() throws Exception {

        Kite request = new Kite(23, "Albacete", "auronplay");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/kite/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La cometa que intentas modificar no existe"));
    }

    //PATCH

    @Test
    @DisplayName("Modifico el viento requerido a una cometa existente")
    @Transactional
    void updateWindRequired() throws Exception {

        KiteUpdatedWindDTO request = new KiteUpdatedWindDTO(30);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/kite/updateWindRequired/9")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.windRequired").value(request.getWindRequired()));

    }

    @Test
    @DisplayName("Modifico el viento requerido a una cometa no existente")
    void updateWindRequiredUnexistingKite() throws Exception {

        KiteUpdatedWindDTO request = new KiteUpdatedWindDTO(30);

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/kite/updateWindRequired/19")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La cometa que intentas modificar no existe"));

    }

    @Test
    @DisplayName("Modifico la ubicación a una cometa existente")
    @Transactional
    void updateLocationKite() throws Exception {

        KiteUpdatedLocationDTO request = new KiteUpdatedLocationDTO("Burgos");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/kite/updateLocation/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value(request.getLocation()));

    }

    @Test
    @DisplayName("Modifico el viento requerido a una cometa no existente")
    void updateLocationUnexistingKite() throws Exception {

        KiteUpdatedLocationDTO request = new KiteUpdatedLocationDTO("Madrid");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/kite/updateLocation/19")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La cometa que intentas modificar no existe"));

    }



    //DELETE

    @Test
    @DisplayName("Elimino una cometa existente")
    @Transactional
    void deleteKite() throws Exception {

        mockMvc.perform(delete("/api/kite/17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Esa cometa ha sido eliminada correctamente"));

    }

    @Test
    @DisplayName("Elimino una cometa inexistente")
    void deleteUnexistingKite() throws Exception {

        mockMvc.perform(delete("/api/kite/27")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La cometa que intentas eliminar no existe"));


    }


}