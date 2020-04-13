package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.UserDto;
import com.bojan.flightadvisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String healthcheck1() {
        return "ok!";
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@NotNull @Valid @RequestBody final UserDto userDto) {
        return userService.register(userDto);

    }

}
