package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.entity.CustomUser;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserMapper {

    public static UserDto toUserDto(final CustomUser user) {
        return UserDto.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password("*****")
                .build();
    }
}
