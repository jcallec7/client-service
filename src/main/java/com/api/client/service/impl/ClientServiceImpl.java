package com.api.client.service.impl;

import com.api.client.dto.ApiResponseDTO;
import com.api.client.dto.ClientCreateDTO;
import com.api.client.dto.ClientResponseDTO;
import com.api.client.dto.ClientUpdateDTO;
import com.api.client.exception.BadRequestException;
import com.api.client.exception.NotFoundException;
import com.api.client.model.Client;
import com.api.client.repository.ClientRepository;
import com.api.client.service.ClientService;
import com.api.client.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static com.api.client.utils.utils.allowedFieldsValidator;
import static com.api.client.utils.utils.getNonNullFields;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClientResponseDTO createClient(ClientCreateDTO clientCreateDTO) throws IllegalAccessException {

        if (clientRepository.existsByIdentificationAndStatusIsTrue(clientCreateDTO.getIdentification())) {
            throw new BadRequestException(messageService.getMessage("identification.already.exist"));
        }

        Optional<Client> clientDisabled = clientRepository.findByIdentificationAndStatusIsFalse(clientCreateDTO.getIdentification());

        if(clientDisabled.isPresent()) {

            ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO();
            clientUpdateDTO.setStatus(true);
            return updateClient(clientDisabled.get().getId(), clientUpdateDTO);

        }

        ModelMapper modelMapper = new ModelMapper();

        clientCreateDTO.setPassword(passwordEncoder.encode(clientCreateDTO.getPassword()));

        Client client = modelMapper.map(clientCreateDTO, Client.class);

        client.setClientId("CLI-" + client.getIdentification());

        if (clientRepository.existsByClientIdAndStatusIsTrue(client.getClientId())) {
            throw new BadRequestException(messageService.getMessage("client.id.already.exist"));
        }

        clientRepository.save(client);

        return modelMapper.map(client, ClientResponseDTO.class);
    }

    public ClientResponseDTO getClientById(Long id) {


        ModelMapper modelMapper = new ModelMapper();

        Optional<Client> client = clientRepository.findByIdAndStatusIsTrue(id);

        if (client.isEmpty()) {
            throw new NotFoundException(messageService.getMessage("register.not.found"));
        }

        return modelMapper.map(client.get(), ClientResponseDTO.class);

    }

    public ClientResponseDTO updateClient(Long id, ClientUpdateDTO clientUpdateDTO) throws IllegalAccessException {

        ModelMapper modelMapper = new ModelMapper();

        StringJoiner fieldsNotAllowed = allowedFieldsValidator(clientUpdateDTO, new HashSet<>(Arrays.asList("clientId", "identification")));

        if (fieldsNotAllowed.length() > 0) {
            Object[] params = {fieldsNotAllowed.toString()};
            throw new BadRequestException(messageService.getMessageWithParams("field.not.allowed.update", params));
        }

        Map<String, Object> nonNullFields = getNonNullFields(clientUpdateDTO);

        Client client = clientRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(messageService.getMessage("register.not.found"))
        );

        for (Map.Entry<String, Object> entry : nonNullFields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("password")) {
                value = passwordEncoder.encode(clientUpdateDTO.getPassword());
            }

            Field field = ReflectionUtils.findField(Client.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, client, value);
            } else {
                Object[] params = {key};
                throw new NotFoundException(messageService.getMessageWithParams("field.not.found", params));
            }
        }

        clientRepository.save(client);

        return modelMapper.map(client, ClientResponseDTO.class);

    }

    @Transactional
    public ApiResponseDTO deleteClient(Long id) throws IllegalAccessException {

        boolean clientExists = clientRepository.existsByIdAndStatusIsTrue(id);

        if (!clientExists) {
            throw new NotFoundException(messageService.getMessage("register.not.found"));
        }

        ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO();
        clientUpdateDTO.setStatus(false);
        updateClient(id, clientUpdateDTO);

        return new ApiResponseDTO(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                messageService.getMessage("register.deleted")
        );
    }
}
