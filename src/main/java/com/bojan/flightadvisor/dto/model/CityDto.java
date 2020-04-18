package com.bojan.flightadvisor.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDto {

    private Long id;
    @NotNull
    @Size(min=2)
    private String name;
    @NotNull
    @Size(min=2)
    private String country;
    @NotNull
    @Size(min=2)
    private String description;

    private List<CityCommentDto> comments;

}
