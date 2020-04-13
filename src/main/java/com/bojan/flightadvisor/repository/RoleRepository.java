package com.bojan.flightadvisor.repository;

import com.bojan.flightadvisor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}