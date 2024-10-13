package com.api.client.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientUpdateDTO {

    private String clientId;
    private String name;
    private String gender;
    private String identification;
    @Positive
    @Min(18)
    @Max(85)
    private Integer age;
    private String address;
    @Size(min = 10)
    private String phoneNumber;
    @Size(min = 8, message = "password must be 8 or more characters")
    private String password;
    private Boolean status;

}
