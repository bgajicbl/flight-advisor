package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.City;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(cityRepository).isNotNull();
    }

    @Test
    void whenSaved_thenFindsByName() {
        cityRepository.save(new City()
                .setName("testCity").setCountry("testCountry").setDescription("testDesc"));
        assertThat(cityRepository.findByNameAndCountry("testCity", "testCountry")).isNotNull();
    }
}
