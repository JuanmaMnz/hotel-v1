package com.hotel.identity_document.Validator;

import com.hotel.Common.Exception.CommonExceptions.DuplicateResourceException;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.identity_document.DTO.IdentityDocumentRequest;
import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Repository.IdentityDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hotel.Common.Exception.ErrorCode.DUPLICATE_RESOURCE;
import static com.hotel.Common.Exception.ErrorCode.INVALID_REQUEST;

@Component
@RequiredArgsConstructor
public class IdentityDocumentValidator {

    private final IdentityDocumentRepository IDRepository;

    public void validateRequest(IdentityDocumentRequest request, Id id){
        validateIfIdentityDocumentExists(id);

        switch (request.getType()) {
            case CEDULA:
                validateCedula(request.getNumber());
                break;

            case CEDULA_EXTRANJERA:
                validateCedulaExtranjera(request.getNumber());
                break;

            case PASAPORTE:
                validatePasaporte(request.getNumber());
                break;

            default:
                throw new InvalidRequestException(INVALID_REQUEST,
                        "Tipo de documento no soportado: " + request.getType());
        }
    }

    private void validateCedula(String number) {
        if (!number.matches("\\d{6,10}")) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La cédula debe contener solo números y tener entre 6 y 10 dígitos.");
        }
    }

    private void validateCedulaExtranjera(String number) {
        if (!number.matches("[A-Z0-9]{5,15}")) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "La cédula extranjera debe contener entre 5 y 15 caracteres alfanuméricos en mayúscula.");
        }
    }

    private void validatePasaporte(String number) {
        if (!number.matches("[A-Z0-9]{6,12}")) {
            throw new InvalidRequestException(INVALID_REQUEST,
                    "El pasaporte debe tener entre 6 y 12 caracteres alfanuméricos en mayúscula.");
        }
    }

    void validateIfIdentityDocumentExists(Id id) {
        if (IDRepository.existsById(id)){
            throw new DuplicateResourceException(DUPLICATE_RESOURCE,
                    "Ya existe un documento de identidad con los datos proporcionados.");
        }
    }

}