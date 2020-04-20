package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.service.CityService;
import com.bojan.flightadvisor.service.FlightService;
import com.bojan.flightadvisor.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER"})
@MockBeans({@MockBean(UserService.class),
        @MockBean(FlightService.class),
        @MockBean(CityService.class)})
class CityControllerTest {

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdminUser_addCity_thenOk() throws Exception {

        CityDto request = CityDto.builder()
                .name("testName").country("testCountry").description("testDesc").build();
        when(cityService.addCity(Mockito.any(CityDto.class))).thenReturn(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        this.mocMvc.perform(post("/api/v1/cities").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"USER"})
    void givenRegularUser_addCity_thenAuthError() throws Exception {

        this.mocMvc.perform(post("/api/v1/cities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllCities() throws Exception {
        this.mocMvc.perform(get("/api/v1/cities"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void searchCities() {
    }

    @Test
    void addComment() {
    }

    @Test
    void deleteComment() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void addAirport() {
    }

    @Test
    void addRoute() {
    }

    @Test
    void searchCheapestRoute() {
    }
}