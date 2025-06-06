package com.hotel.Common.Seed;

import com.hotel.Employee.DTO.EmployeeRequest;
import com.hotel.Employee.Service.IEmployeeService;
import com.hotel.Guest.DTO.GuestRequest;
import com.hotel.Guest.DTO.RegisterGuestRequest;
import com.hotel.Guest.Service.GuestRegistrationService;
import com.hotel.Person.DTO.PersonRequest;
import com.hotel.Person.Enums.Gender;
import com.hotel.Person.Repository.PersonRepository;
import com.hotel.Security.Account.DTO.AccountRequest;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.DTO.IdentityDocumentRequest;
import com.hotel.identity_document.Enums.Type;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final GuestRegistrationService guestRegistrationService;
    private final IEmployeeService employeeService;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Override
    public void run(String... args) throws Exception {

        if (activeProfile.equals("dev")) {
            logger.info("""
                Usuario de prueba:
                Email= 'employeeone@email.com',
                Password= 'employee123!',
                Roles= [GUEST, EMPLOYEE].
                """);
            if (personRepository.count() == 0) {
                seedEmployees();
            }
        }
    }

    @Transactional
    public void seedEmployees() {

        // Employee 1
        IdentityDocumentRequest identityDocumentRequest = IdentityDocumentRequest.builder()
                .type(Type.CEDULA)
                .number("123456789")
                .issuingCountry("COLOMBIA")
                .issueDate(LocalDate.of(2020, 9, 20))
                .build();

        PersonRequest personRequest = PersonRequest.builder()
                .firstName("Employee")
                .lastName("One")
                .phoneNumber("+573547652987")
                .gender(Gender.MASCULINO)
                .birthOfDate(LocalDate.of(2005, 9, 20))
                .build();

        AccountRequest accountRequest = AccountRequest.builder()
                .email("employeeone@email.com")
                .password("employee123!")
                .build();

        GuestRequest guestRequest = GuestRequest.builder()
                .address("Carrera 10 #12-A57")
                .build();

        RegisterGuestRequest request = RegisterGuestRequest.builder()
                .identityDocument(identityDocumentRequest)
                .personalInformation(personRequest)
                .accountInformation(accountRequest)
                .guestExtraInformation(guestRequest)
                .build();

        guestRegistrationService.registerGuest(request);

        IdRequest personId = IdRequest.builder()
                .type(Type.CEDULA)
                .number("123456789")
                .build();

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .personId(personId)
                .startDate(LocalDate.now())
                .build();

        employeeService.createAndSaveEmployee(employeeRequest);
    }

}