package com.hotel.Common.Exception.CommonExceptions;

import com.hotel.Common.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateResourceException(ErrorCode errorCode, String message){
        super(errorCode.getMessage() + ", " + message);
        this.errorCode = errorCode;
    }

}