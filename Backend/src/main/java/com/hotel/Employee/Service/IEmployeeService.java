package com.hotel.Employee.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Employee.DTO.EmployeeRequest;
import com.hotel.Employee.DTO.EmployeeUpdateRequest;
import com.hotel.Employee.Model.Employee;
import com.hotel.identity_document.Model.Id;

public interface IEmployeeService {

    Employee createNewEmployee(EmployeeRequest request);

    Response createAndSaveEmployee(EmployeeRequest request);

    Employee getEmployeeById(Id employeeId);

    Response getAllEmployeesToResponse(PageableRequest pageable);

    Response getEmployeeToResponseById(Id employeeId);

    Response updateEmployeeToRetired(Id employeeId, EmployeeUpdateRequest request);

}