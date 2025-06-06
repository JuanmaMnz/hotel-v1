package com.hotel.Security.Account.Repository;

import com.hotel.Security.Account.Model.Account;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Id> {

    boolean existsByEmail(String email);

    @Query("""
    SELECT a
    FROM Account a
    JOIN FETCH a.person p
    LEFT JOIN FETCH p.employee e
    LEFT JOIN FETCH p.guest g
    WHERE a.email = :email
    """)
    Optional<Account> findAccountWithRelationsByEmail(@Param("email") String email);

}