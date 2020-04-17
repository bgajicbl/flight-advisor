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
@Table(name = "airport")
public class Airport {

    @Id
    private long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private City city;
    @Column(nullable = false)
    private String iata;
    @Column(nullable = false)
    private String icao;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private int altitude;
    @Column
    private String timezone;
    @Column
    private String dst;
    @Column
    private String tzOlson;
    @Column
    private String type;
    @Column
    private String source;

}
