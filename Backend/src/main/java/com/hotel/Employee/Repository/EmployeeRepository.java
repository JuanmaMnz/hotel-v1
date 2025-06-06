package com.hotel.Employee.Repository;

import com.hotel.Employee.Model.Employee;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Id> {

    @Query("""
           SELECT e
           FROM Employee e
           JOIN FETCH e.person p
           JOIN FETCH p.identityDocument id
           WHERE e.employeeId = :employeeId
           """)
    Optional<Employee> findEmployeeWithPerson(@Param("employeeId") Id employeeId);

    @Query(
            value = """
        SELECT e
        FROM Employee e
        JOIN FETCH e.person p
        JOIN FETCH p.identityDocument
        """,
            countQuery = "SELECT count(e) FROM Employee e"
    )
    Page<Employee> findAllWithPersonAndDocument(Pageable pageable);

    Optional<Employee> findEmployeeByPerson(Person person);

}