package com.hotel.Person.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Response.Response;
import com.hotel.Person.DTO.PersonRequest;
import com.hotel.Person.Model.Person;
import com.hotel.Person.Repository.PersonRepository;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Person.Messages.PersonResponseMessages.PERSON_UPDATED_SUCCESSFULLY;
import static com.hotel.identity_document.Utils.IdUtils.buildId;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements IPersonService {

    private final PersonRepository personRepository;

    @Override
    public Person getPersonById(Id id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El usuario con el ID '"+ id.getType() + ": " + id.getNumber() + "' no fue encontrado"));
    }

    @Override
    public Person createNewPerson(PersonRequest request, IdentityDocument identityDocument) {
        return Person.builder()
                .personId(identityDocument.getId())
                .identityDocument(identityDocument)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .birthOfDate(request.getBirthOfDate())
                .build();
    }

    @Override
    public Person savePerson(PersonRequest request, IdentityDocument identityDocument) {
        Person person = createNewPerson(request, identityDocument);
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Response updatePersonalInformation(IdRequest personToBeUpdated, PersonRequest request) {
        Id personId = buildId(personToBeUpdated.getType(), personToBeUpdated.getNumber());
        Person person = getPersonById(personId);

        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setGender(request.getGender());
        person.setBirthOfDate(request.getBirthOfDate());

        return generateResponse(OK, PERSON_UPDATED_SUCCESSFULLY);
    }

}