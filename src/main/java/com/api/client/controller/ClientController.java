package com.api.client.controller;

import com.api.client.dto.ApiResponseDTO;
import com.api.client.dto.ClientCreateDTO;
import com.api.client.dto.ClientResponseDTO;
import com.api.client.dto.ClientUpdateDTO;
import com.api.client.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/clientes")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/crear")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateDTO clientCreateDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientCreateDTO));

    }

    @GetMapping("/obtenerPorId/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {

        return ResponseEntity.ok(clientService.getClientById(id));

    }

    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @RequestBody @Valid ClientUpdateDTO clientUpdateDTO) throws IllegalAccessException {

        return ResponseEntity.ok(clientService.updateClient(id, clientUpdateDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDTO> deleteClient(@PathVariable Long id) throws IllegalAccessException {

        return ResponseEntity.ok(clientService.deleteClient(id));
    }
}
