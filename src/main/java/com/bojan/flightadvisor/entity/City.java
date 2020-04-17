package com.bojan.flightadvisor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "city")
public class  City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String description;
    @OneToMany(mappedBy="city")
    @OrderBy("modifiedAt DESC")
    private SortedSet<CityComment> comments = new TreeSet<>();

}
