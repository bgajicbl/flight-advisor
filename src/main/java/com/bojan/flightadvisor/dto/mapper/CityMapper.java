package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.City;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CityMapper {

    public static CityDto toCityDto(City city) {
        return CityDto.builder()
                .name(city.getName())
                .country(city.getCountry())
                .description(city.getDescription())
                .build();
    }
}
