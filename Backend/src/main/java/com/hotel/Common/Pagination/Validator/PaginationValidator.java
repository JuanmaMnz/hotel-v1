package com.hotel.Common.Pagination.Validator;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;

@Component
public class PaginationValidator {

    public void validatePagination( int pageNumber, int pageSize) {
        validatePageNumber(pageNumber);
        validatePageSize(pageSize);
    }

    public void validatePageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "El número de página no puede ser negativo.");
        }
    }

    public void validatePageNumber(int pageNumber) {
        if (pageNumber < 0 || pageNumber > 100) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "El tamaño de página debe ser entre 1 y 50.");
        }
    }

}
