package com.hotel.Invoice.Service;

import com.hotel.Common.Response.Response;
import com.hotel.Invoice.DTO.InvoiceRequest;
import com.hotel.Invoice.Model.Invoice;

public interface IInvoiceService {

    Invoice createInvoice(InvoiceRequest request);

    Invoice getInvoice(Integer invoiceId);

    Invoice createAndSaveInvoice(InvoiceRequest request);

    Response getInvoiceById(Integer invoiceId);

}