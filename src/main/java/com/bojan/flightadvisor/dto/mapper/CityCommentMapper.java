package com.bojan.flightadvisor.dto.mapper;

import com.bojan.flightadvisor.dto.model.CityCommentDto;
import com.bojan.flightadvisor.entity.CityComment;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CityCommentMapper {

    public static CityCommentDto toCityCommentDto(final CityComment cityComment) {
        return CityCommentDto.builder()
                .id(cityComment.getId())
                .cityId(cityComment.getCity().getId())
                .comment(cityComment.getComment())
                .timeCreated(cityComment.getCreatedAt().toString())
                .timeModified(cityComment.getModifiedAt().toString())
                .build();
    }
}
