package com.hotel.Employee.Mapper;

import com.hotel.Employee.DTO.EmployeeResponse;
import com.hotel.Employee.Model.Employee;
import com.hotel.Person.Mapper.PersonMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface EmployeeMapper {

    @Mapping(source = "person", target = "personalInformation")
    @Mapping(source = "active", target = "employeeExtraInformation.isActive")
    @Mapping(source = "startDate", target = "employeeExtraInformation.startDate")
    @Mapping(source = "terminationDate", target = "employeeExtraInformation.terminationDate")
    EmployeeResponse toEmployeeResponse(Employee employee);
}