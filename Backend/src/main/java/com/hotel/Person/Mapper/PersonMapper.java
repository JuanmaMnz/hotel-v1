package com.hotel.Person.Mapper;

import com.hotel.Person.DTO.PersonResponse;
import com.hotel.Person.Model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonResponse toPersonResponse(Person person);
}
