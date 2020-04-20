package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.mapper.CityCommentMapper;
import com.bojan.flightadvisor.dto.mapper.CityMapper;
import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.dto.model.CityCommentUpdateDto;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.entity.CityComment;
import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.exception.EntityExistsException;
import com.bojan.flightadvisor.exception.EntityNotFoundException;
import com.bojan.flightadvisor.repository.AirportRepository;
import com.bojan.flightadvisor.repository.CityCommentRepository;
import com.bojan.flightadvisor.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

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

    /**
     * Handle addition of a new city
     *
     * @param cityDto CityDto
     * @return
     */
    @Override
    public CityDto addCity(final CityDto cityDto) {
        Optional<City> cityOpt = cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry());
        if (cityOpt.isPresent()) {
            throw new EntityExistsException(City.class, "name", cityDto.getName(), "country", cityDto.getCountry());
        }
        City city = new City()
                .setName(cityDto.getName())
                .setCountry(cityDto.getCountry())
                .setDescription(cityDto.getDescription());

        return CityMapper.toCityDto(cityRepository.save(city));
    }

    /**
     * Handle addition of a new airport
     *
     * @param airportDto AirportDto
     * @return the AirportDto object
     */
    @Override
    public AirportDto addAirport(final AirportDto airportDto) {
        City city = cityRepository.findByNameAndCountry(airportDto.getCity(), airportDto.getCountry())
                .orElseThrow(() -> new EntityNotFoundException(City.class, "city", airportDto.getCity(), "country", airportDto.getCountry()));

        Airport airport = new Airport()
                .setId(airportDto.getId())
                .setName(airportDto.getName())
                .setCity(city)
                .setIata(airportDto.getIata())
                .setIcao(airportDto.getIcao());
        airportRepository.save(airport);

        return airportDto;
    }

    /**
     * Handle city search by name
     *
     * @param nameOpt city name
     * @param commentsNum number of comments to be returned
     * @return list of CityDto
     */
    @Override
    public List<CityDto> searchCities(final String name, final Optional<Integer> commentsNum) {
        List<City> cities = cityRepository.findByNameIgnoreCase(name);

        if (cities.isEmpty()) {
            return Collections.emptyList();
        }
        //filter number of comments
        filterCityComments(cities, commentsNum);
        return cities
                .stream()
                .map(city -> CityMapper.toCityDto(city))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param commentsNum
     * @param pageable
     * @return
     */
    @Override
    public List<CityDto> getAllCities(final Optional<Integer> commentsNum, Pageable pageable) {
        Page<City> cityPage = cityRepository.findAll(pageable);

        if (!cityPage.hasContent()) {
            return Collections.emptyList();
        }
        List<City> cities = cityPage.getContent();
        //filter number of comments
        filterCityComments(cities, commentsNum);

        return cities
                .stream()
                .map(city -> CityMapper.toCityDto(city))
                .collect(Collectors.toList());
    }

    private void filterCityComments(List<City> cityList, Optional<Integer> num) {
        if (num.isPresent()) {
            for (City city : cityList) {
                SortedSet<CityComment> cityComments = city.getComments().stream().limit(num.get())
                        .collect(Collectors.toCollection(TreeSet::new));
                city.setComments(cityComments);
            }
        }
    }

    /**
     * Handles add comment
     *
     * @param commentDto
     * @param user
     * @return the CityCommentDto object
     */
    @Override
    public CityCommentDto addComment(final CityCommentDto commentDto, final CustomUser user) {
        City city = cityRepository.findById(commentDto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(City.class, "cityId", commentDto.getCityId().toString()));

        Optional<CityComment> cityCommentOpt = cityCommentRepository.findByCityAndUser(city, user);
        if (cityCommentOpt.isPresent()) {
            throw new EntityExistsException(CityComment.class, "user", user.getUsername(), "city", city.getName());
        }
        CityComment cityComment = new CityComment()
                .setCity(city)
                .setUser(user)
                .setComment(commentDto.getComment())
                .setCreatedAt(LocalDateTime.now())
                .setModifiedAt(LocalDateTime.now());
        return CityCommentMapper.toCityCommentDto(cityCommentRepository.save(cityComment));
    }

    /**
     * Handles comment deletion
     *
     * @param commentId
     * @param user Authenticated user
     * @return String message
     */
    @Override
    public void deleteComment(final Long commentId, final CustomUser user) {
        CityComment cityComment = cityCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(CityComment.class, "id", commentId.toString()));

        if (user.equals(cityComment.getUser())) {
            cityCommentRepository.delete(cityComment);
        } else {
            throw new AccessDeniedException("Can't delete comments from other users!");
        }
    }

    /**
     * Handles comment update
     *
     * @param commentId
     * @param comment
     * @param user
     * @return the CityCommentDto object
     */
    @Override
    public CityCommentDto updateComment(final Long commentId, final CityCommentUpdateDto comment, final CustomUser user) {
        CityComment cityComment = cityCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(CityComment.class, "id", commentId.toString()));

        if (user.equals(cityComment.getUser())) {
            cityComment.setComment(comment.getComment());
            cityComment.setModifiedAt(LocalDateTime.now());

            return CityCommentMapper.toCityCommentDto(cityCommentRepository.save(cityComment));
        } else {
            throw new AccessDeniedException("Can't edit comments from other users!");
        }
    }

}
