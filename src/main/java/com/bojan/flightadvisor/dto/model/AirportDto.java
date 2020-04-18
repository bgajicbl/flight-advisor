package com.bojan.flightadvisor.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirportDto {

    @NotNull
    private Long id;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String city;
    @NotNull
    @Size(min = 2)
    private String country;
    @NotNull
    @Size(max = 3)
    private String iata;
    @NotNull
    @Size(max = 4)
    private String icao;

    private Double latitude;

    private Double longitude;

    private int altitude;

    private String timezone;

    private String dst;

    private String tzOlson;

    private String type;

    private String source;
}
