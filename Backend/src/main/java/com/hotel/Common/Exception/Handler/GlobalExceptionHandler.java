package com.hotel.Common.Exception.Handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hotel.Common.Exception.CommonExceptions.DuplicateResourceException;
import com.hotel.Common.Exception.CommonExceptions.InvalidRequestException;
import com.hotel.Common.Exception.CommonExceptions.ResourceNotFoundException;
import com.hotel.Common.Exception.ErrorCode;
import com.hotel.Common.Exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hotel.Common.Exception.ErrorCode.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSourceException(InvalidRequestException ex, HttpServletRequest request){
        return buildErrorResponse(
                request.getRequestURI(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
            org.springframework.security.authorization.AuthorizationDeniedException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                request.getRequestURI(),
                ACCESS_DENIED,
                ACCESS_DENIED.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(
                request.getRequestURI(),
                UNAUTHORIZED,
                "Credenciales incorrectas. Verifica tu correo y contraseña.",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof InvalidFormatException invalidFormatEx) {
            Class<?> targetType = invalidFormatEx.getTargetType();

            if (targetType.isEnum()) {
                String invalidValue = invalidFormatEx.getValue().toString();
                String acceptedValues = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                String message = String.format(
                        "Valor '%s' no es válido. Valores permitidos: [%s]",
                        invalidValue, acceptedValues
                );

                return buildErrorResponse(
                        request.getRequestURI(),
                        ErrorCode.INVALID_REQUEST,
                        message,
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        return buildErrorResponse(
                request.getRequestURI(),
                ErrorCode.INVALID_REQUEST,
                "Error de formato en el cuerpo de la petición",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        Set<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    String fieldName = fieldError.getField().substring(fieldError.getField().lastIndexOf('.') + 1);
                    String errorMessage = fieldError.getDefaultMessage();
                    return "Error en el campo '" + fieldName + "', " + errorMessage;
                })
                .collect(Collectors.toSet());

        return buildErrorResponse(
                request.getRequestURI(),
                INVALID_REQUEST,
                messages,
                INVALID_REQUEST.getStatus()
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSourceException(DuplicateResourceException ex, HttpServletRequest request){
        return buildErrorResponse(
                request.getRequestURI(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        return buildErrorResponse(
                request.getRequestURI(),
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Set<String> messages = ex.getConstraintViolations()
                .stream()
                .map(cv -> "Error en el campo '" + cv.getPropertyPath() + "', " + cv.getMessage())
                .collect(Collectors.toSet());

        return buildErrorResponse(
                request.getRequestURI(),
                INVALID_REQUEST,
                messages,
                INVALID_REQUEST.getStatus()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(
                request.getRequestURI(),
                DATABASE_ERROR,
                "Violación de integridad de datos: " + ex.getRootCause().getMessage(),
                DATABASE_ERROR.getStatus()
        );
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTransactionSystemException(TransactionSystemException ex, HttpServletRequest request) {
        return buildErrorResponse(
                request.getRequestURI(),
                DATABASE_ERROR,
                "Error en la transacción. Verifica los datos enviados.",
                DATABASE_ERROR.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                request.getRequestURI(),
                INTERNAL_ERROR,
                "Ocurrió un error inesperado: " + ex.getMessage(),
                INTERNAL_ERROR.getStatus()
        );
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(String path, ErrorCode error, String message, HttpStatus status) {
        Set<String> messages = new HashSet<>();
        messages.add(message);
        ErrorResponse errorResponse = new ErrorResponse(path, error, messages, status, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String path, ErrorCode error, Set<String> messages, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(path, error, messages, status, LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, status);
    }

}