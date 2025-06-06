package com.hotel.Common.Exception.CommonExceptions;

import com.hotel.Common.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class CloudinaryException extends RuntimeException {

    private final ErrorCode errorCode;

    public CloudinaryException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode.getMessage() + ", " + message, cause);
        this.errorCode = errorCode;
    }

}