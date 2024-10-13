package com.api.client.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true)
public class Client extends User {

    @Column(unique = true, nullable = false)
    private String clientId;
    private String password;
    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean status = true;

}
