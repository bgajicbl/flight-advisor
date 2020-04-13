package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto register(@NotNull @Valid @RequestBody final CityDto cityDto) {
        return cityService.addCity(cityDto);

    }

}
