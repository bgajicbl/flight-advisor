package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.FlightDto;
import com.bojan.flightadvisor.dto.model.RouteDto;
import com.bojan.flightadvisor.processor.FlightNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class FlightMapper {

    public static FlightDto toFlightDto(final FlightNode flightNode) {
        List<RouteDto> cheapestRoutes = flightNode.getCheapestPathRoutes().stream()
                .map(RouteMapper::toCityDto).collect(Collectors.toList());

        return FlightDto.builder()
                .totalPrice(flightNode.getPrice())
                .routes(cheapestRoutes)
                .build();
    }
}
