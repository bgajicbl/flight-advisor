package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.mapper.CityCommentMapper;
import com.bojan.flightadvisor.dto.mapper.CityMapper;
import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.entity.CityComment;
import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.exception.EntityAlreadyExistException;
import com.bojan.flightadvisor.repository.AirportRepository;
import com.bojan.flightadvisor.repository.CityCommentRepository;
import com.bojan.flightadvisor.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityCommentRepository cityCommentRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public CityDto addCity(final CityDto cityDto) {
        Optional<City> cityOpt = cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry());
        if (cityOpt.isPresent()) {
            throw new EntityAlreadyExistException(String.format("There is a city with name %s and country %s.",
                    cityDto.getName(), cityDto.getCountry()));
        }
        City city = new City()
                .setName(cityDto.getName())
                .setCountry(cityDto.getCountry())
                .setDescription(cityDto.getDescription());

        return CityMapper.toCityDto(cityRepository.save(city));
    }

    @Override
    public AirportDto addAirport(final AirportDto airportDto) {
        City city = cityRepository.findByNameAndCountry(airportDto.getCity(), airportDto.getCountry())
                .orElseThrow(() -> new EntityNotFoundException("City not found!"));

        Airport airport = new Airport()
                .setId(airportDto.getId())
                .setName(airportDto.getName())
                .setCity(city)
                .setIata(airportDto.getIata())
                .setIcao(airportDto.getIcao());
        airportRepository.save(airport);

        return airportDto;
    }

    @Override
    public List<CityDto> searchCities(final Optional<String> nameOpt, final Optional<Integer> commentsNum) {
        List<City> cities = null;
        if (nameOpt.isPresent()) {
            cities = cityRepository.findByNameIgnoreCase(nameOpt.get());
        } else {
            cities = cityRepository.findAll();
        }
        if (cities.isEmpty()) {
            return Collections.emptyList();
        }

        if (commentsNum.isPresent()) {
            List<City> filteredCities = new ArrayList<>();
            for (City city : cities) {
                filterCityComments(city, commentsNum.get());
                filteredCities.add(city);
            }
            cities = filteredCities;
        }
        return cities
                .stream()
                .map(city -> CityMapper.toCityDto(city))
                .collect(Collectors.toList());

    }

    private void filterCityComments(final City city, final int number) {
        SortedSet<CityComment> cityComments = city.getComments().stream().limit(number)
                .collect(Collectors.toCollection(TreeSet::new));
        city.setComments(cityComments);

    }

    @Override
    public CityCommentDto addComment(final CityCommentDto commentDto, final CustomUser user) {
        City city = cityRepository.findById(commentDto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found!"));

        Optional<CityComment> cityCommentOpt = cityCommentRepository.findByCityAndUser(city, user);
        if (cityCommentOpt.isPresent()) {
            throw new EntityAlreadyExistException(String.format("There is a comment from user %s for city %s.",
                    user.getUsername(), city.getName()));
        }
        CityComment cityComment = new CityComment()
                .setCity(city)
                .setUser(user)
                .setComment(commentDto.getComment())
                .setCreatedAt(LocalDateTime.now())
                .setModifiedAt(LocalDateTime.now());
        return CityCommentMapper.toCityCommentDto(cityCommentRepository.save(cityComment));
    }

    @Override
    public String deleteComment(final Long commentId, final CustomUser user) {
        CityComment cityComment = cityCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found!"));

        if (user.equals(cityComment.getUser())) {
            cityCommentRepository.delete(cityComment);
            return "Deleted!";
        } else {
            throw new AccessDeniedException("Can't delete comments from other users!");
        }
    }

    @Override
    public CityCommentDto updateComment(final Long commentId, final CityCommentDto commentDto, final CustomUser user) {
        CityComment cityComment = cityCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found!"));

        if (user.equals(cityComment.getUser())) {
            cityComment.setComment(commentDto.getComment());
            cityComment.setModifiedAt(LocalDateTime.now());

            return CityCommentMapper.toCityCommentDto(cityCommentRepository.save(cityComment));
        } else {
            throw new AccessDeniedException("Can't edit comments from other users!");
        }
    }

}
