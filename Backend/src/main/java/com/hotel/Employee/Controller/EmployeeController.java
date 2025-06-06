package com.hotel.Employee.Controller;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Employee.DTO.EmployeeRequest;
import com.hotel.Employee.DTO.EmployeeUpdateRequest;
import com.hotel.Employee.Service.IEmployeeService;
import com.hotel.identity_document.Enums.Type;
import com.hotel.identity_document.Model.Id;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.hotel.Common.Pagination.Utils.PaginationUtils.buildPageableRequest;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "Operaciones relacionadas con los empleados")
public class EmployeeController {

    private final IEmployeeService employeeService;

    @PostMapping("/employee")
    @Operation(summary = "Registrar un nuevo empleado")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> createEmployee(
            @Valid @RequestBody EmployeeRequest request
    ) {
        Response response = employeeService.createAndSaveEmployee(request);
        return ResponseEntity.status(CREATED).body(response);
    }


    @GetMapping("/employee")
    @Operation(summary = "Obtener los datos de un empleado por ID")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getEmployeeById(

            @NotNull(message = "El tipo de documento no puede ser nulo")
            @RequestParam Type type,

            @NotBlank(message = "El número de documento no puede estar vacío")
            @RequestParam String number
    ) {
        Id id = buildId(type, number);
        Response response = employeeService.getEmployeeToResponseById(id);
        return ResponseEntity.status(OK).body(response);
    }


    @GetMapping("/employees")
    @Operation(summary = "Obtener todos los empleados")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> getAllEmployees(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageableRequest pageableRequest = buildPageableRequest(pageNumber, pageSize);
        Response response = employeeService.getAllEmployeesToResponse(pageableRequest);
        return ResponseEntity.status(OK).body(response);
    }


    @PutMapping("/employee/retire")
    @Operation(summary = "Marcar a un empleado como retirado")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Response> retireEmployee(
            @RequestParam Type type,
            @RequestParam String number,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        Id employeeId = buildId(type, number);
        Response response = employeeService.updateEmployeeToRetired(employeeId, request);
        return ResponseEntity.status(OK).body(response);
    }

}