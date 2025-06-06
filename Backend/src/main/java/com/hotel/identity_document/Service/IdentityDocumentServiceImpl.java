package com.hotel.identity_document.Service;

import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.identity_document.DTO.IdentityDocumentRequest;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;
import com.hotel.identity_document.Repository.IdentityDocumentRepository;
import com.hotel.identity_document.Validator.IdentityDocumentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hotel.Common.Exception.ErrorCode.RESOURCE_NOT_FOUND;
import static com.hotel.identity_document.Utils.IdUtils.buildId;

@Service
@RequiredArgsConstructor
public class IdentityDocumentServiceImpl implements IIdentityDocumentService {

    private final IdentityDocumentRepository IDRepository;
    private final IdentityDocumentValidator identityDocumentValidator;

    @Override
    public IdentityDocument getIdentityDocumentById(Id id) {
        return IDRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND,
                        "El documento de identidad con el ID '"+ id.getType() + ": " + id.getNumber() + "' no fue encontrado"));
    }

    @Override
    public IdentityDocument createNewIdentityDocument(IdentityDocumentRequest request) {
        Id id = buildId(request.getType(), request.getNumber());
        identityDocumentValidator.validateRequest(request, id);
        return IdentityDocument.builder()
                .id(id)
                .issuingCountry(request.getIssuingCountry().toUpperCase())
                .issueDate(request.getIssueDate())
                .build();
    }

    @Override
    public IdentityDocument saveIdentityDocument(IdentityDocumentRequest request) {

        IdentityDocument identityDocument = createNewIdentityDocument(request);
        return IDRepository.save(identityDocument);
    }

}