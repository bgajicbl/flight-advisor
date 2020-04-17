package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RouteRepository extends JpaRepository<Route, Long> {

    Route findBySourceAirportAndDestinationAirport(Airport sourceAirport, Airport destinationAirport);
}