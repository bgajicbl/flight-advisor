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
public class RouteDto {

    @NotNull
    @Size(min = 2, max = 3)
    private String airline;
    @NotNull
    private Long airlineId;
    @NotNull
    @Size(min = 2)
    private String sourceAirport;
    @NotNull
    private Long sourceAirportId;
    @NotNull
    @Size(min = 2)
    private String destinationAirport;
    @NotNull
    private Long destinationAirportId;

    private String codeShare;

    private int stops;

    private String equipment;

    @NotNull
    private Double price;

}
