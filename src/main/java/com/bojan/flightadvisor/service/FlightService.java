package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.model.FlightDto;

public interface FlightService {

    public FlightDto calculateCheapest(Long cityFrom, Long cityTo);

}
