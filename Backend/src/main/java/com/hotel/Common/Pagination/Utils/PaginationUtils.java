package com.hotel.Common.Pagination.Utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Common.Pagination.DTO.PageableRequest;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginationUtils {

    public static PageableRequest buildPageableRequest(int pageNumber, int pageSize) {
        return PageableRequest.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }

}