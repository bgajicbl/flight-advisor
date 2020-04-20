package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.authentication.UserDetailsMapper;
import com.bojan.flightadvisor.dto.mapper.UserMapper;
import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.entity.Role;
import com.bojan.flightadvisor.exception.EntityExistsException;
import com.bojan.flightadvisor.repository.RoleRepository;
import com.bojan.flightadvisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    /**
     * Handles user registration
     *
     * @param userDto
     * @return the UserDto object
     */
    @Override
    public UserDto register(final UserDto userDto) {
        Optional<CustomUser> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isPresent()) {
            throw new EntityExistsException(CustomUser.class, "username", userDto.getUsername());
        }
        Role userRole = roleRepository.findByName("ROLE_USER");
        CustomUser user = new CustomUser()
                .setUsername(userDto.getUsername())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(passwordEncoder.encode(userDto.getPassword()))
                .setRoles(new HashSet<>(Arrays.asList(userRole)));
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public CustomUser findUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found:" + username));
    }

}
