package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.RouteDto;
import com.bojan.flightadvisor.entity.Route;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class RouteMapper {

    public static RouteDto toCityDto(final Route route) {
        return RouteDto.builder()
                .airlineId(route.getAirline().getId())
                .airline(route.getAirline().getName())
                .sourceAirportId(route.getSourceAirport().getId())
                .sourceAirport(route.getSourceAirport().getName())
                .destinationAirportId(route.getDestinationAirport().getId())
                .destinationAirport(route.getDestinationAirport().getName())
                .stops(route.getStops())
                .price(route.getPrice())
                .build();
    }

}
