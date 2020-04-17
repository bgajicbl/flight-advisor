package com.bojan.flightadvisor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "city_comment", uniqueConstraints = {@UniqueConstraint(columnNames = {"city_id", "user_id"})})
public class CityComment implements Comparable<CityComment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    @Override
    public int compareTo(final CityComment cityComment) {
        return cityComment.getModifiedAt().compareTo(this.getModifiedAt());
    }
}
