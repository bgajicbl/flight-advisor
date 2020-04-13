package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.mapper.UserMapper;
import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.entity.Role;
import com.bojan.flightadvisor.exception.EntityAlreadyExistException;
import com.bojan.flightadvisor.repository.RoleRepository;
import com.bojan.flightadvisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserDto register(UserDto userDto) {
        CustomUser user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            throw new EntityAlreadyExistException("There is an account with that username: " + userDto.getUsername());
        }
        Role userRole = roleRepository.findByName("ROLE_USER");
        user = new CustomUser()
                .setUsername(userDto.getUsername())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(passwordEncoder.encode(userDto.getPassword()))
                .setRoles(new HashSet<>(Arrays.asList(userRole)));
        return UserMapper.toUserDto(userRepository.save(user));
    }
}
