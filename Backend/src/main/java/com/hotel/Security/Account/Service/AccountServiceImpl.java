package com.hotel.Security.Account.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Security.Account.DTO.AccountRequest;
import com.hotel.Security.Account.Model.Account;
import com.hotel.Security.Account.Repository.AccountRepository;
import com.hotel.Security.Account.Validator.AccountValidator;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.Model.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountValidator accountValidator;

    @Override
    public Account createNewAccount(AccountRequest request, Id id, Person person) {
        accountValidator.validateRequest(request, id);
        return Account.builder()
                .accountId(id)
                .person(person)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enable(true)
                .build();
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findAccountWithRelationsByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "No se encontró una cuenta registrada con el correo proporcionado"));
    }

    @Override
    public void saveAccount(AccountRequest request, Id id, Person person) {
        Account account= createNewAccount(request, id, person);
        accountRepository.save(account);
    }

}