package com.hotel.Security.Account.Service;

import com.hotel.Security.Account.DTO.AccountRequest;
import com.hotel.Security.Account.Model.Account;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.Model.Id;

public interface IAccountService {

    Account createNewAccount(AccountRequest request, Id id, Person person);

    Account getAccountByEmail(String email);

    void saveAccount(AccountRequest request, Id id, Person person);
}