package com.hotel.Guest.DTO;

import com.hotel.Security.Account.DTO.AccountRequest;
import com.hotel.Person.DTO.PersonRequest;
import com.hotel.identity_document.DTO.IdentityDocumentRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterGuestRequest {

    @Valid private IdentityDocumentRequest identityDocument;

    @Valid private PersonRequest personalInformation;

    @Valid private AccountRequest accountInformation;

    @Valid private GuestRequest guestExtraInformation;

}