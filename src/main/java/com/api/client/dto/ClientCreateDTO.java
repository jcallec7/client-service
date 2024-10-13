package com.api.client.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientCreateDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String gender;
    @Positive
    @NotNull
    @Min(18)
    @Max(85)
    private Integer age;
    @NotBlank
    @Size(min = 10)
    private String identification;
    @NotBlank
    private String address;
    @NotBlank
    @Size(min = 10)
    private String phoneNumber;
    @NotBlank
    @Size(min = 8, message = "password must be 8 or more characters")
    private String password;

}
