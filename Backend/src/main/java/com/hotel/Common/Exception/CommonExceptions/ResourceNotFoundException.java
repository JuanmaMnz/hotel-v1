package com.hotel.Common.Exception.CommonExceptions;

import com.hotel.Common.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + ", " + message);
        this.errorCode = errorCode;
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
