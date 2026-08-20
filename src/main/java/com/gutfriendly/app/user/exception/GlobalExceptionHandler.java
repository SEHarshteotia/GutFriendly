package com.gutfriendly.app.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handles missing database resources.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.NOT_FOUND
        );
    }

    // Handles invalid input and invalid business requests.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    // Handles duplicate data or conflicting business states.
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            org.springframework.web.server.ResponseStatusException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getStatusCode().value(),
                exception.getStatusCode().toString(),
                exception.getReason(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                exception.getStatusCode()
        );
    }

    // Handles a UNIQUE / NOT NULL / foreign-key violation that reached the
    // database. The exact column is worked out below so the caller is told
    // which detail is already taken instead of a blanket "server error".
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                describeDataIntegrityViolation(exception),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.CONFLICT
        );
    }

    private String describeDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException exception) {

        String cause = exception.getMostSpecificCause().getMessage();
        String haystack = cause == null ? "" : cause.toLowerCase();

        if (haystack.contains("phone")) {
            return "That phone number is already registered";
        }
        if (haystack.contains("email") || haystack.contains("mail")) {
            return "That email address is already registered";
        }
        if (haystack.contains("adhar") || haystack.contains("aadhaar")
                || haystack.contains("aadhar")) {
            return "That Aadhaar number is already registered";
        }
        if (haystack.contains("pan")) {
            return "That PAN number is already registered";
        }
        if (haystack.contains("licen")) {
            return "That licence number is already registered";
        }
        if (haystack.contains("gst")) {
            return "That GST number is already registered";
        }
        if (haystack.contains("cannot be null") || haystack.contains("not-null")
                || haystack.contains("null value")) {
            return "A required field was left empty. Please fill in every "
                    + "required detail and try again.";
        }
        if (haystack.contains("foreign key")) {
            return "This record is linked to other data and cannot be saved "
                    + "or removed as-is.";
        }

        return "Some of these details are already registered to another "
                + "account. Please check your phone number, email, Aadhaar "
                + "and PAN.";
    }

    // Handles a request body that could not be read, e.g. an unknown value for
    // a dropdown / enum field. Without this it would surface as a 500.
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(
            org.springframework.http.converter.HttpMessageNotReadableException exception,
            HttpServletRequest request) {

        String detail = "The request could not be read. Please check the "
                + "values you selected and try again.";

        Throwable cause = exception.getMostSpecificCause();
        String causeText = cause.getMessage() == null ? "" : cause.getMessage();

        // Jackson reports an unknown dropdown value as
        // "... not one of the values accepted for Enum class: [A, B]".
        int acceptedAt = causeText.indexOf("not one of the values accepted");

        if (acceptedAt >= 0) {
            int quoteStart = causeText.indexOf('"');
            int quoteEnd = causeText.indexOf('"', quoteStart + 1);
            String badValue = quoteStart >= 0 && quoteEnd > quoteStart
                    ? causeText.substring(quoteStart + 1, quoteEnd)
                    : "That value";
            detail = "\"" + badValue + "\" is not a valid option for this field.";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail,
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    // A path or query parameter of the wrong type, e.g. /shops/abc.
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "\"" + exception.getValue() + "\" is not a valid value for "
                        + exception.getName() + ".",
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    // Services throw this for input they consider unusable; the message is
    // written for the caller, so pass it through rather than hiding it.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        String detail = exception.getMessage() == null
                || exception.getMessage().isBlank()
                        ? "Invalid request. Please check your input."
                        : exception.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail,
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.BAD_REQUEST
        );
    }

    // Handles unexpected errors without exposing the stack trace.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception exception,
            HttpServletRequest request) {

        // The caller gets a safe message, but the real cause has to be in the
        // server log or these are impossible to diagnose in production.
        log.error("Unhandled exception for {} {}",
                request.getMethod(), request.getRequestURI(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong on the server",
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}