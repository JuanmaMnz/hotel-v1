package com.hotel.Person.DTO;

import com.hotel.identity_document.DTO.IdRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonUpdateRequest {

    @Valid IdRequest personToBeUpdate;

    @Valid PersonRequest request;

}