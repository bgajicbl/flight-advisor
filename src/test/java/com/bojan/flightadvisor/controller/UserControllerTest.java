package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.UserDto;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@MockBeans({@MockBean(UserService.class)})
class UserControllerTest {

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    private UserService userService;

    @Test
    void register() throws Exception {
        UserDto request = UserDto.builder()
                .username("test").firstName("testName").lastName("testSurname").password("test").build();
        when(userService.register(Mockito.any(UserDto.class))).thenReturn(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        this.mocMvc.perform(post("/api/v1/users/register").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(request.getUsername()));

    }
}