package com.bojan.flightadvisor.processor;

import com.bojan.flightadvisor.entity.Route;

import java.util.*;

public class FlightNode {

    private Long airportId;

    private List<FlightNode> cheapestPath = new LinkedList<>();

    private List<Route> cheapestPathRoutes = new ArrayList<>();

    private Double price = Double.POSITIVE_INFINITY;

    private Map<FlightNode, Route> adjacentNodes = new HashMap<>();

    public FlightNode(Long airportId) {
        this.airportId = airportId;
    }

    public void addDestination(FlightNode destination, Route route) {

        adjacentNodes.put(destination, route);
    }

    public Long getAirportId() {
        return airportId;
    }

    public void setAirportId(Long airportId) {

        this.airportId = airportId;
    }

    public Map<FlightNode, Route> getAdjacentNodes() {

        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<FlightNode, Route> adjacentNodes) {

        this.adjacentNodes = adjacentNodes;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {

        this.price = price;
    }

    public List<FlightNode> getCheapestPath() {

        return cheapestPath;
    }

    public void setCheapestPath(List<FlightNode> cheapestPath) {

        this.cheapestPath = cheapestPath;
    }
    public List<Route> getCheapestPathRoutes() {

        return cheapestPathRoutes;
    }

    public void setCheapestPathRoutes(ArrayList<Route> cheapestPathRoutes) {
        
        this.cheapestPathRoutes = cheapestPathRoutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightNode node = (FlightNode) o;
        return airportId.equals(node.airportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportId);
    }
}
