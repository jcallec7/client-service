package com.api.client.controller;

import com.api.client.dto.ClientCreateDTO;
import com.api.client.dto.ClientResponseDTO;
import com.api.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    void createClientSuccessfully() throws Exception {

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO();
        clientCreateDTO.setName(faker.name().name());
        clientCreateDTO.setGender("male");
        clientCreateDTO.setAge(faker.number().numberBetween(1, 85));
        clientCreateDTO.setIdentification(faker.number().digits(10));
        clientCreateDTO.setAddress(faker.address().streetAddress());
        clientCreateDTO.setPhoneNumber(faker.phoneNumber().phoneNumber());
        clientCreateDTO.setPassword(faker.internet().password());

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setName(clientCreateDTO.getName());
        clientResponseDTO.setGender(clientCreateDTO.getGender());
        clientResponseDTO.setAge(clientCreateDTO.getAge());
        clientResponseDTO.setIdentification(clientCreateDTO.getIdentification());
        clientResponseDTO.setAddress(clientCreateDTO.getAddress());
        clientResponseDTO.setPhoneNumber(clientCreateDTO.getPhoneNumber());

        when(clientService.createClient(clientCreateDTO)).thenReturn(clientResponseDTO);

        mockMvc.perform(post("/api/clientes/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(clientResponseDTO.getName()));

    }
}