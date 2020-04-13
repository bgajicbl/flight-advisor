package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.mapper.CityMapper;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.exception.EntityAlreadyExistException;
import com.bojan.flightadvisor.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public CityDto addCity(CityDto cityDto) {
        City city = cityRepository.findByNameAndCountry(cityDto.getName(), cityDto.getCountry());
        if (city != null) {
            throw new EntityAlreadyExistException(String.format("There is a city with name %s and country %s.",
                    cityDto.getName(), cityDto.getCountry()));
        }
        city = new City()
                .setName(cityDto.getName())
                .setCountry(cityDto.getCountry())
                .setDescription(cityDto.getDescription());

        return CityMapper.toCityDto(cityRepository.save(city));
    }
}
