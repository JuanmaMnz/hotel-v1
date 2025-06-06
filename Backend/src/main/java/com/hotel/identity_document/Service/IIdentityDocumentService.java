package com.hotel.identity_document.Service;

import com.hotel.identity_document.DTO.IdentityDocumentRequest;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;

public interface IIdentityDocumentService {

    IdentityDocument getIdentityDocumentById(Id id);

    IdentityDocument createNewIdentityDocument(IdentityDocumentRequest request);

    IdentityDocument saveIdentityDocument(IdentityDocumentRequest request);
}