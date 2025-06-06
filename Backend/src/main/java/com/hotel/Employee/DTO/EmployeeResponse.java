package com.hotel.Employee.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hotel.Person.DTO.PersonResponse;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EmployeeResponse(

        PersonResponse personalInformation,

        EmployeePartialResponse employeeExtraInformation

) implements Serializable {
}