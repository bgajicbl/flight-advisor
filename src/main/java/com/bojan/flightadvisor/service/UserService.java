package com.bojan.flightadvisor.service;

import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.entity.CustomUser;

public interface UserService {

    UserDto register(UserDto userDto);

    CustomUser findUserByUsername(String username);


}
