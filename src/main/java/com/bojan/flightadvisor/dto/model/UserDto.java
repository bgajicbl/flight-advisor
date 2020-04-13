package com.bojan.flightadvisor.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @NotNull
    @Size(min=2, max=30)
    private String username;
    @NotNull
    @Size(min=2, max=30)
    private String firstName;
    @NotNull
    @Size(min=2, max=30)
    private String lastName;
    @NotNull
    @Size(min=2, max=30)
    private String password;

}
