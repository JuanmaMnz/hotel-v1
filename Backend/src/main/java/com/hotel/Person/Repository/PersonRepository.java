package com.hotel.Person.Repository;

import com.hotel.Person.Model.Person;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Id> {

}
