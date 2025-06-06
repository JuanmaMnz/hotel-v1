package com.hotel.Common.Exception.CommonExceptions;

import com.hotel.Common.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class RateLimitExceededException extends RuntimeException {

    private final ErrorCode errorCode;

    public RateLimitExceededException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + ", " + message);
        this.errorCode = errorCode;
    }

}