package com.bojan.flightadvisor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="airline", nullable=false)
    private Airline airline;

    @ManyToOne
    @JoinColumn(name="source_airport", nullable=false)
    private Airport sourceAirport;

    @ManyToOne
    @JoinColumn(name="destination_airport", nullable=false)
    private Airport destinationAirport;

    @Column
    private String codeShare;
    @Column
    private int stops;
    @Column
    private String equipment;
    @Column(nullable = false)
    private Double price;

}
