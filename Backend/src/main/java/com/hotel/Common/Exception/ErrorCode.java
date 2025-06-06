package com.hotel.Common.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    RESOURCE_NOT_FOUND (NOT_FOUND, "No se encontro el recurso"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"),
    DUPLICATE_RESOURCE (CONFLICT, "El recurso que intenta registrar ya existe"),
    DATABASE_ERROR(CONFLICT, "Error de base de datos"),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "Error interno"),
    RESOURCE_CONFLICT(CONFLICT, "Conflicto con el estado actual del recurso"),
    ACTION_NOT_ALLOWED(FORBIDDEN, "No tiene permitido realizar esta acción"),
    CLOUDINARY_ERROR(INTERNAL_SERVER_ERROR, "Error al procesar la imagen con Cloudinary"),
    ACCESS_DENIED(FORBIDDEN, "No tienes permiso para acceder a este recurso"),
    RATE_LIMIT_EXCEEDED (TOO_MANY_REQUESTS, "Límite de peticiones excedido"),
    INVALID_REQUEST (BAD_REQUEST, "Campo inválido o faltante");

    private final HttpStatus status;
    private final String message;
}