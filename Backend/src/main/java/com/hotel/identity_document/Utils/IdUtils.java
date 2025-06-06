package com.hotel.identity_document.Utils;

import com.hotel.identity_document.Enums.Type;
import com.hotel.identity_document.Model.Id;

public class IdUtils {

    public static Id buildId(Type type, String number){
        return Id.builder()
                .type(type)
                .number(number)
                .build();
    }

}