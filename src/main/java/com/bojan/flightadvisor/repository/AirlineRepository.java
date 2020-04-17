package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.Airline;
import com.bojan.flightadvisor.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Airport findByName(String name);

}