package com.bojan.flightadvisor.processor;

import com.bojan.flightadvisor.entity.Route;

import java.util.*;

public class FlightPriceProcessor {

    public static FlightGraph calculateCheapestPathFromSource(FlightGraph graph, FlightNode source) {

        source.setPrice(0.0);

        Set<FlightNode> settledNodes = new HashSet<>();
        Set<FlightNode> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);

        while (!unsettledNodes.isEmpty()) {
            FlightNode currentNode = getLowestPriceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<FlightNode, Route> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                FlightNode adjacentNode = adjacencyPair.getKey();
                Route edgeRoute = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumPrice(adjacentNode, edgeRoute, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void CalculateMinimumPrice(FlightNode evaluationNode, Route route, FlightNode sourceNode) {
        Double sourcePrice = sourceNode.getPrice();
        Double edgeWeigh = route.getPrice();
        if (sourcePrice + edgeWeigh < evaluationNode.getPrice()) {
            evaluationNode.setPrice(sourcePrice + edgeWeigh);
            LinkedList<FlightNode> shortestPath = new LinkedList<>(sourceNode.getCheapestPath());
            ArrayList<Route> shortestPathRoute = new ArrayList<>(sourceNode.getCheapestPathRoutes());
            shortestPath.add(sourceNode);
            shortestPathRoute.add(route);
            evaluationNode.setCheapestPath(shortestPath);
            evaluationNode.setCheapestPathRoutes(shortestPathRoute);
        }
    }

    private static FlightNode getLowestPriceNode(Set<FlightNode> unsettledNodes) {
        FlightNode lowestPriceNode = null;
        double lowestPrice = Double.POSITIVE_INFINITY;
        for (FlightNode node : unsettledNodes) {
            double nodePrice = node.getPrice();
            if (nodePrice < lowestPrice) {
                lowestPrice = nodePrice;
                lowestPriceNode = node;
            }
        }
        return lowestPriceNode;
    }
}
