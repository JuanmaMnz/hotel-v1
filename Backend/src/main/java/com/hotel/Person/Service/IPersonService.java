package com.hotel.Person.Service;

import com.hotel.Common.Response.Response;
import com.hotel.Person.DTO.PersonRequest;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;

public interface IPersonService {

    Person getPersonById(Id id);

    Person createNewPerson(PersonRequest request, IdentityDocument identityDocument);

    Person savePerson(PersonRequest request, IdentityDocument identityDocument);

    Response updatePersonalInformation(IdRequest personToBeUpdated, PersonRequest request);

}