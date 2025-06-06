package com.hotel.Common.Exception;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record ErrorResponse(
        String path,

        @Enumerated(EnumType.STRING)
        ErrorCode error,

        Set<String> messages,

        HttpStatus status,

        LocalDateTime localDateTime
) {
}
