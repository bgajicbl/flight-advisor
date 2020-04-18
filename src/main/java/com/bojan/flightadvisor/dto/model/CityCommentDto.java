package com.bojan.flightadvisor.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityCommentDto {

    private Long id;
    @NotNull
    private Long cityId;
    @NotEmpty(message = "Comment may not be empty!")
    private String comment;
    private String timeCreated;
    private String timeModified;

}
