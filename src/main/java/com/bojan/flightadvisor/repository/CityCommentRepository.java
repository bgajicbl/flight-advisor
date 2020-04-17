package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.entity.CityComment;
import com.bojan.flightadvisor.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CityCommentRepository extends JpaRepository<CityComment, Long> {

    List<CityComment> findByCity(City city);
    Optional<CityComment> findByCityAndUser(City city, CustomUser user);
}