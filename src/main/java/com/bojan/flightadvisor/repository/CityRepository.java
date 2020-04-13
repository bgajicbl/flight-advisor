package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CityRepository extends JpaRepository<City, Long> {

    City findByName(String name);
    City findByNameAndCountry(String name, String country);
}