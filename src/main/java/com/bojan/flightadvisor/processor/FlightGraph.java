package com.bojan.flightadvisor.processor;

import java.util.HashSet;
import java.util.Set;

public class FlightGraph {

    private Set<FlightNode> nodes = new HashSet<>();

    public void addNode(FlightNode nodeA) {
        nodes.add(nodeA);
    }

    public Set<FlightNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<FlightNode> nodes) {
        this.nodes = nodes;
    }
}
