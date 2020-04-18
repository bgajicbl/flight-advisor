package com.bojan.flightadvisor.controller;

import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.dto.model.CityDto;
import com.bojan.flightadvisor.dto.model.FileImport;
import com.bojan.flightadvisor.dto.model.FlightDto;
import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.service.CityService;
import com.bojan.flightadvisor.service.FlightService;
import com.bojan.flightadvisor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importAirportJob")
    private Job airportJob;

    @Autowired
    @Qualifier("importRouteJob")
    private Job routeJob;

    @Autowired
    private CityService cityService;

    @Operation(summary = "Add new city", description = "Adding a new city", tags = { "city" })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto addCity(@NotNull @Valid @RequestBody final CityDto cityDto) {

        return cityService.addCity(cityDto);
    }

    @Operation(summary = "Get all cities", description = "Get all cities", tags = { "city" })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CityDto> getAllCities(@RequestParam final Optional<Integer> commentsNum) {
        return cityService.searchCities(Optional.empty(), commentsNum);
    }

    @Operation(summary = "Search for city by name", description = "Search for city by name", tags = { "city" })
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<CityDto> searchCities(@PathVariable @NotNull final String name, @RequestParam final Optional<Integer> commentsNum) {

        return cityService.searchCities(Optional.of(name), commentsNum);

    }

    @Operation(summary = "Add a comment", description = "Add a comment to a city", tags = { "comment" })
    @PostMapping("/comment/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CityCommentDto addComment(@NotNull @Valid @RequestBody final CityCommentDto comment) {

        CustomUser user = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return cityService.addComment(comment, user);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment to a city", tags = { "comment" })
    @DeleteMapping("/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@PathVariable @NotNull final Long id) {

        CustomUser user = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return cityService.deleteComment(id, user);
    }

    @Operation(summary = "Edit a comment", description = "Edit a comment to a city", tags = { "comment" })
    @PutMapping("/comment/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityCommentDto updateComment(@NotNull @Valid @RequestBody final CityCommentDto comment, @PathVariable @NotNull final Long id) {

        CustomUser user = userService.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return cityService.updateComment(id, comment, user);
    }

    @Operation(summary = "Add an airport", description = "Add a new airport", tags = { "airport" })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/airport/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String addAirport(@NotNull @RequestBody final FileImport file)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", file.getPath())
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(airportJob, jobParameters);

        return "ok";
    }

    @Operation(summary = "Add a route", description = "Add a new route", tags = { "route" })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/route/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String addRoute(@NotNull @RequestBody final FileImport file)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", file.getPath())
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(routeJob, jobParameters);

        return "ok";

    }

    @Operation(summary = "Find a cheapest route", description = "Find a cheapest route", tags = { "route" })
    @GetMapping("/route/from/{cityFrom}/to/{cityTo}")
    @ResponseStatus(HttpStatus.OK)
    public FlightDto searchCheapestRoute(@PathVariable @NotNull final Long cityFrom, @PathVariable @NotNull final Long cityTo) {
        return flightService.calculateCheapest(cityFrom, cityTo);
    }

}
