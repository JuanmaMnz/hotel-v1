package com.hotel.Employee.Validator;

import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Employee.DTO.EmployeeRequest;
import com.hotel.Employee.Repository.EmployeeRepository;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.DUPLICATE_RESOURCE;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final EmployeeRepository employeeRepository;

    public void validateRequest(Id personId, EmployeeRequest request) {

        if (employeeRepository.existsById(personId)) {
            throw new InvalidRequestException(
                    DUPLICATE_RESOURCE,
                    "Ya existe un empleado registrado con el ID proporcionado.");
        }
    }

}