package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.City;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CityRepository extends PagingAndSortingRepository<City, Long> {

    List<City> findByNameIgnoreCase(String name);
    Optional<City> findByNameAndCountry(String name, String country);
}