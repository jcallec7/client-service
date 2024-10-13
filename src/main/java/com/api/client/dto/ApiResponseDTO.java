package com.api.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponseDTO {
    private Integer code;
    private HttpStatus status;
    private String message;
}
