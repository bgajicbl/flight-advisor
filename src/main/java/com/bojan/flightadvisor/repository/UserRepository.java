package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByUsername(String username);
}