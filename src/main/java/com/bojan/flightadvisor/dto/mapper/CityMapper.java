package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.City;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 *
 */
@Component
public class CityMapper {

    public static CityDto toCityDto(final City city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry())
                .description(city.getDescription())
                .comments(city.getComments().stream().map(x -> CityCommentMapper.toCityCommentDto(x)).collect(Collectors.toList()))
                .build();
    }

}
