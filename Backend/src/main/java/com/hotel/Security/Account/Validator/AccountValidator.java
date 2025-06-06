package com.hotel.Security.Account.Validator;

import com.hotel.Security.Account.DTO.AccountRequest;
import com.hotel.Security.Account.Repository.AccountRepository;
import com.hotel.Common.Exception.CommonExceptions.DuplicateResourceException;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.DUPLICATE_RESOURCE;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final AccountRepository accountRepository;

    public void validateRequest(AccountRequest request, Id id) {
        validateUniqueEmail(request.getEmail());
        accountExistsById(id);
    }

    void accountExistsById(Id id) {
        if ( accountRepository.existsById(id) ){
            throw new DuplicateResourceException(DUPLICATE_RESOURCE,
                    "Ya existe una cuenta con el documento de identidad proporcionado.");
        }
    }

    void validateUniqueEmail(String email){
        if ( accountRepository.existsByEmail(email) ){
            throw new DuplicateResourceException(DUPLICATE_RESOURCE,
                    "Ya existe una cuenta con el email proporcionado.");
        }
    }

}