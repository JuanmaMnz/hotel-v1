package com.hotel.Common.Pagination.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagedResponse<T> (
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) implements Serializable {
}