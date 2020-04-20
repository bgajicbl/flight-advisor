package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.dto.model.CityCommentUpdateDto;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.CustomUser;

import java.util.List;
import java.util.Optional;

public interface CityService {

    CityDto addCity(CityDto cityDto);

    AirportDto addAirport(AirportDto airportDto);

    List<CityDto> searchCities(Optional<String> nameOpt, Optional<Integer> commentsNum);

    CityCommentDto addComment(CityCommentDto commentDto, CustomUser user);

    String deleteComment(Long commentId, CustomUser user);

    CityCommentDto updateComment(Long id, CityCommentUpdateDto comment, CustomUser user);
}
