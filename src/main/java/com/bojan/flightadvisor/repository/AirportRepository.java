package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Airport findByName(String name);
    Airport findByIata(String iata);
    Airport findByIcao(String icao);
    List<Airport> findByCity(City city);
}