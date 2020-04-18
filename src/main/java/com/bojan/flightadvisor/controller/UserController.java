package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "the User API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register new user", description = "Registration of a new user", tags = { "user" })
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@NotNull @Valid @RequestBody final UserDto userDto) {
        return userService.register(userDto);
    }

}
