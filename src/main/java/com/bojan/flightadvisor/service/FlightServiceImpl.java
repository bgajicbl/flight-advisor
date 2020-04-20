package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.mapper.FlightMapper;
import com.bojan.flightadvisor.dto.model.FlightDto;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.entity.Route;
import com.bojan.flightadvisor.exception.EntityNotFoundException;
import com.bojan.flightadvisor.processor.FlightGraph;
import com.bojan.flightadvisor.processor.FlightNode;
import com.bojan.flightadvisor.processor.FlightPriceProcessor;
import com.bojan.flightadvisor.repository.AirportRepository;
import com.bojan.flightadvisor.repository.CityRepository;
import com.bojan.flightadvisor.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class FlightServiceImpl implements FlightService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private RouteRepository routeRepository;

    /**
     * Handles cheapest flight calculation
     *
     * @param cityFromId
     * @param cityToId
     * @return the FlightDto object
     */
    @Override
    public FlightDto calculateCheapest(final Long cityFromId, final Long cityToId) {

        City originCity = cityRepository.findById(cityFromId)
                .orElseThrow(() -> new EntityNotFoundException(City.class, "cityFromId", cityFromId.toString()));
        City destinationCity = cityRepository.findById(cityToId)
                .orElseThrow(() -> new EntityNotFoundException(City.class, "cityToId", cityToId.toString()));

        List<Airport> originAirports = airportRepository.findByCity(originCity);
        if (originAirports.isEmpty()) {
            throw new EntityNotFoundException(Airport.class, "cityFromId", cityFromId.toString());
        }
        List<Airport> destinationAirports = airportRepository.findByCity(destinationCity);
        if (destinationAirports.isEmpty()) {
            throw new EntityNotFoundException(Airport.class, "cityFromId", cityToId.toString());
        }
        final List<Route> routes = routeRepository.findAll();
        final List<Airport> airports = airportRepository.findAll();
        //create all nodes, identifier is Airport ID
        HashMap<Long, FlightNode> allNodesMap = new HashMap<>();
        for (Airport airport : airports) {
            FlightNode node = new FlightNode(airport.getId());
            allNodesMap.put(airport.getId(), node);
        }

        for (Route route : routes) {
            allNodesMap.get(route.getSourceAirport().getId())
                    .addDestination(allNodesMap.get(route.getDestinationAirport().getId()), route);
        }

        //add all nodes to graph
        FlightGraph finalGraph = new FlightGraph();
        allNodesMap.forEach((key, value) -> {
            finalGraph.addNode(value);
        });
        //city can have multiple airports, so we need to create graph for every origin airport
        Double cheapestFlight = Double.POSITIVE_INFINITY;
        FlightNode cheapestNode = null;
        for (Airport originAirport : originAirports) {
            FlightGraph resultGraph = FlightPriceProcessor.calculateCheapestPathFromSource(finalGraph, allNodesMap.get(originAirport.getId()));
            for (Airport destinationAirport : destinationAirports) {
                Optional<FlightNode> resNode = resultGraph.getNodes().stream()
                        .filter(x -> allNodesMap.get(destinationAirport.getId()).equals(x)).findFirst();
                if (resNode.isPresent() && (resNode.get().getPrice() < cheapestFlight)) {
                    cheapestFlight = resNode.get().getPrice();
                    cheapestNode = resNode.get();
                }
            }
        }
        if (cheapestNode == null) {
            throw new EntityNotFoundException(Route.class, "cityFromId", cityFromId.toString(), "cityFromId", cityToId.toString());
        }

        return FlightMapper.toFlightDto(cheapestNode);
    }
}
