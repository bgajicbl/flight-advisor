package com.bojan.flightadvisor.processor;

import com.bojan.flightadvisor.dto.model.RouteDto;
import com.bojan.flightadvisor.entity.Airline;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.Route;
import com.bojan.flightadvisor.repository.AirlineRepository;
import com.bojan.flightadvisor.repository.AirportRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RouteItemProcessor implements ItemProcessor<RouteDto, Route> {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public Route process(final RouteDto routeDto) throws Exception {
        Airline airline = null;
        Optional<Airline> airlineOpt = airlineRepository.findById(routeDto.getAirlineId());
        if (airlineOpt.isPresent()) {
            airline = airlineOpt.get();
        } else {
            airline = new Airline()
                    .setId(routeDto.getAirlineId())
                    .setName(routeDto.getAirline());
            airline = airlineRepository.save(airline);
        }
        Optional<Airport> sourceAirportOpt = airportRepository.findById(routeDto.getSourceAirportId());
        if(!sourceAirportOpt.isPresent()){
            return null;
        }
        Airport sourceAirport = sourceAirportOpt.get();

        Optional<Airport> destinationAirportOpt = airportRepository.findById(routeDto.getDestinationAirportId());
        if(!destinationAirportOpt.isPresent()){
            return null;
        }
        Airport destinationAirport = destinationAirportOpt.get();

        Route route = new Route()
                .setAirline(airline)
                .setSourceAirport(sourceAirport)
                .setDestinationAirport(destinationAirport)
                .setCodeShare(routeDto.getCodeShare())
                .setStops(routeDto.getStops())
                .setEquipment(routeDto.getEquipment())
                .setPrice(routeDto.getPrice());

        return route;
    }
}
