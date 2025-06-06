package com.hotel.Common.Exception.CommonExceptions;

import com.hotel.Common.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException{

    private final ErrorCode errorCode;

    public InvalidRequestException(ErrorCode errorCode){
      super(errorCode.getMessage());
      this.errorCode = errorCode;
    }

    public InvalidRequestException(ErrorCode errorCode, String message){
      super(errorCode.getMessage() + ", " + message);
      this.errorCode = errorCode;
    }

}