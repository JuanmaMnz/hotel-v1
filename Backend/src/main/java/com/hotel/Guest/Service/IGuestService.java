package com.hotel.Guest.Service;

import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;
import com.hotel.Guest.DTO.GuestUpdateRequest;
import com.hotel.Guest.Model.Guest;
import com.hotel.Person.Model.Person;
import com.hotel.identity_document.DTO.IdRequest;
import com.hotel.identity_document.Model.Id;

public interface IGuestService {

    Guest getGuestById(Id guestId);

    Guest createNewGuest(Id id, Person person, String address);

    void createAndSaveGuest(Id id, Person person, String address);

    Response getGuestToResponseById(IdRequest guestId);

    Response updateGuest(GuestUpdateRequest request);

    Response getAllGuests(PageableRequest request);
}