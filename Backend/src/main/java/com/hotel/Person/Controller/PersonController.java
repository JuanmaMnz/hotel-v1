package com.hotel.Person.Controller;

import com.hotel.Common.Response.Response;
import com.hotel.Person.DTO.PersonUpdateRequest;
import com.hotel.Person.Service.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Persona", description = "Operaciones relacionadas con la información personal")
public class PersonController {

    private final IPersonService service;

    @PutMapping("/person")
    @Operation(summary = "Actualizar información personal de una persona")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> updatePersonalInformation(
            @Valid @RequestBody PersonUpdateRequest personUpdateRequest
    ) {
        Response response = service.updatePersonalInformation(
                personUpdateRequest.getPersonToBeUpdate(),
                personUpdateRequest.getRequest());
        return ResponseEntity.status(OK).body(response);
    }

}