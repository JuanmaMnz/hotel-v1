package com.hotel.Guest.Service;

import com.hotel.Security.Account.Service.IAccountService;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.DTO.RegisterGuestRequest;
import com.hotel.Person.Model.Person;
import com.hotel.Person.Service.IPersonService;
import com.hotel.identity_document.Model.IdentityDocument;
import com.hotel.identity_document.Service.IIdentityDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Guest.Messages.GuestResponseMessages.GUEST_REGISTERED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
public class GuestRegistrationService {

    private final IIdentityDocumentService identityDocumentService;
    private final IPersonService personService;
    private final IAccountService accountService;
    private final IGuestService guestService;

    @Transactional
    public Response registerGuest(RegisterGuestRequest request) {

        IdentityDocument identityDocument = identityDocumentService
                .saveIdentityDocument(request.getIdentityDocument());

        Person person = personService
                .savePerson(request.getPersonalInformation(), identityDocument);

        accountService
                .saveAccount(request.getAccountInformation(), identityDocument.getId(), person);

        guestService.createAndSaveGuest(
                identityDocument.getId(), person, request.getGuestExtraInformation().getAddress());

        return generateResponse(CREATED, GUEST_REGISTERED_SUCCESSFULLY);
    }

}