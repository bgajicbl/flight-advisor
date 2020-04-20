package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.dto.model.CityCommentUpdateDto;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.CustomUser;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CityService {

    CityDto addCity(CityDto cityDto);

    AirportDto addAirport(AirportDto airportDto);

    List<CityDto> searchCities(String name, Optional<Integer> commentsNum);

    List<CityDto> getAllCities(Optional<Integer> commentsNum, Pageable pageable);

    CityCommentDto addComment(CityCommentDto commentDto, CustomUser user);

    void deleteComment(Long commentId, CustomUser user);

    CityCommentDto updateComment(Long id, CityCommentUpdateDto comment, CustomUser user);
}
