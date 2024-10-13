package com.api.client.service.impl;

import com.api.client.dto.ClientCreateDTO;
import com.api.client.dto.ClientResponseDTO;
import com.api.client.exception.BadRequestException;
import com.api.client.model.Client;
import com.api.client.repository.ClientRepository;
import com.api.client.service.MessageService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageService messageService;

    private final Faker faker = new Faker();


    @Test
    void createClientSuccessfully() throws IllegalAccessException {

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO();
        clientCreateDTO.setIdentification(faker.number().digits(10));
        clientCreateDTO.setPassword(faker.internet().password(8,16));

        when(clientRepository.existsByIdentificationAndStatusIsTrue(clientCreateDTO.getIdentification())).thenReturn(false);
        when(passwordEncoder.encode(clientCreateDTO.getPassword())).thenReturn("encodedPassword");

        Client client = new Client();
        client.setIdentification(clientCreateDTO.getIdentification());
        client.setClientId("CLI-" +clientCreateDTO.getIdentification());

        when(clientRepository.existsByClientIdAndStatusIsTrue(client.getClientId())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponseDTO response = clientService.createClient(clientCreateDTO);

        assertNotNull(response);
        assertEquals(client.getClientId(), response.getClientId());
        verify(clientRepository).save(any(Client.class));

    }

    @Test
    void createClientIdentificationExist() {

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO();
        clientCreateDTO.setIdentification(faker.number().digits(10));

        when(clientRepository.existsByIdentificationAndStatusIsTrue(clientCreateDTO.getIdentification())).thenReturn(true);
        when(messageService.getMessage("identification.already.exist")).thenReturn("Identification already registered.");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            clientService.createClient(clientCreateDTO);
        });
        assertEquals("Identification already registered.", exception.getMessage());


    }

    @Test
    void createClientClientIdExist() {

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO();
        clientCreateDTO.setIdentification(faker.number().digits(10));
        clientCreateDTO.setPassword(faker.internet().password(8,16));

        when(clientRepository.existsByIdentificationAndStatusIsTrue(clientCreateDTO.getIdentification())).thenReturn(false);
        when(passwordEncoder.encode(clientCreateDTO.getPassword())).thenReturn("encodedPassword");

        Client client = new Client();
        client.setIdentification(clientCreateDTO.getIdentification());
        client.setClientId("CLI-" +clientCreateDTO.getIdentification());

        when(clientRepository.existsByClientIdAndStatusIsTrue(client.getClientId())).thenReturn(true);
        when(messageService.getMessage("client.id.already.exist")).thenReturn("ClientId already registered.");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            clientService.createClient(clientCreateDTO);
        });
        assertEquals("ClientId already registered.", exception.getMessage());

    }
}