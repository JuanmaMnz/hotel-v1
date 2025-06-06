package com.hotel.Common.Pagination.Mapper;

import com.hotel.Common.Pagination.DTO.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PagedResponseMapper {

    public <T> PagedResponse<T> toPagedResponse(Page<T> pageData) {
        return new PagedResponse<>(
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages());
    }

}