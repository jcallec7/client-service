package com.api.client.service;

import com.api.client.dto.ApiResponseDTO;
import com.api.client.dto.ClientCreateDTO;
import com.api.client.dto.ClientResponseDTO;
import com.api.client.dto.ClientUpdateDTO;

public interface ClientService {
    ClientResponseDTO createClient(ClientCreateDTO clientCreateDTO);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientUpdateDTO updateClientDto) throws IllegalAccessException;
    ApiResponseDTO deleteClient(Long id) throws IllegalAccessException;
}
