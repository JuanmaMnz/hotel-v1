package com.hotel.Employee.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Pagination.DTO.PagedResponse;
import com.hotel.Common.Pagination.Mapper.PagedResponseMapper;
import com.hotel.Common.Pagination.Validator.PaginationValidator;
import com.hotel.Common.Response.Response;
import com.hotel.Employee.DTO.EmployeeRequest;
import com.hotel.Employee.DTO.EmployeeResponse;
import com.hotel.Employee.DTO.EmployeeUpdateRequest;
import com.hotel.Employee.Mapper.EmployeeMapper;
import com.hotel.Employee.Validator.EmployeeValidator;
import com.hotel.Person.Model.Person;
import com.hotel.Person.Service.IPersonService;
import com.hotel.identity_document.Model.Id;
import com.hotel.Employee.Model.Employee;
import com.hotel.Employee.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Employee.Messages.EmployeeResponseMessages.*;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService{

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final IPersonService personService;
    private final PagedResponseMapper pagedResponseMapper;
    private final PaginationValidator paginationValidator;

    @Override
    public Employee createNewEmployee(EmployeeRequest request) {
        Id id = buildId(request.getPersonId().getType(), request.getPersonId().getNumber());
        Person person = personService.getPersonById(id);

        employeeValidator.validateRequest(id, request);
        return Employee.builder()
                .employeeId(person.getPersonId())
                .person(person)
                .startDate(request.getStartDate())
                .isActive(true)
                .build();
    }

    @Override
    public Response createAndSaveEmployee(EmployeeRequest request) {
        Employee employee = createNewEmployee(request);
        employeeRepository.save(employee);
        return generateResponse(CREATED, EMPLOYEE_REGISTERED_SUCCESSFULLY);
    }

    @Override
    public Employee getEmployeeById(Id employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El empleado con ID '" + employeeId + "' no fue encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Response getAllEmployeesToResponse(PageableRequest pageableRequest) {
        paginationValidator.validatePagination(pageableRequest.getPageNumber(), pageableRequest.getPageSize());

        Pageable pageable = PageRequest.of(
                pageableRequest.getPageNumber(),
                pageableRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "employeeId")
        );

        Page<Employee> employeesPage = employeeRepository.findAllWithPersonAndDocument(pageable);
        Page<EmployeeResponse> employeeResponses = employeesPage.map(employeeMapper::toEmployeeResponse);
        PagedResponse<EmployeeResponse> response = pagedResponseMapper.toPagedResponse(employeeResponses);
        return generateResponse(response, OK, EMPLOYEES_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    public Response getEmployeeToResponseById(Id employeeId) {
        Employee employee = employeeRepository.findEmployeeWithPerson(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El empleado con ID '" + employeeId + "' no fue encontrado"));

        EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);
        return generateResponse(employeeResponse, OK, EMPLOYEE_RETRIEVED_SUCCESSFULLY);
    }

    @Override
    @Transactional
    public Response updateEmployeeToRetired(Id employeeId, EmployeeUpdateRequest request) {
        Employee employee = getEmployeeById(employeeId);

        employee.setActive(false);
        employee.setTerminationDate(request.getTerminationDate());

        return generateResponse(OK, EMPLOYEE_UPDATED_SUCCESSFULLY);
    }

}