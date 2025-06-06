package com.hotel.CheckOut.Service;

import com.hotel.CheckOut.DTO.CheckOutRequest;
import com.hotel.CheckOut.DTO.CheckOutUpdateRequest;
import com.hotel.CheckOut.Model.CheckOut;
import com.hotel.Common.Pagination.DTO.PageableRequest;
import com.hotel.Common.Response.Response;

import java.time.LocalDate;

public interface ICheckOutService {

    CheckOut getCheckOutById(Integer checkOutId);

    CheckOut createCheckOut(CheckOutRequest request);

    Response getCheckOutResponseById(Integer checkOutId);

    Response registerCheckOut(CheckOutRequest request);

    Response updateCheckOut(Integer checkOutId, CheckOutUpdateRequest request);

    Response getAllCheckOutsByDate(PageableRequest pageableRequest, LocalDate date);

}