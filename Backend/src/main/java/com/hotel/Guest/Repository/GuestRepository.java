package com.hotel.Guest.Repository;

import com.hotel.Guest.Model.Guest;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Id> {

    @Query("""
    SELECT g
    FROM Guest g
    JOIN FETCH g.person p
    JOIN FETCH p.identityDocument
    WHERE g.guestId = :id
    """)
    Optional<Guest> findByIdWithPerson(@Param("id") Id id);

    @Query(
            value = """
        SELECT g FROM Guest g
        JOIN FETCH g.person p
        JOIN FETCH p.identityDocument
        """,
            countQuery = "SELECT count(g) FROM Guest g"
    )
    Page<Guest> findAllWithPersonAndDocument(Pageable pageable);

}