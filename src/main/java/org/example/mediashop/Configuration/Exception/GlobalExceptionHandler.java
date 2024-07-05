package org.example.mediashop.Configuration.Exception;

import jakarta.validation.ConstraintViolationException;
import org.example.mediashop.Payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method handles the NotFoundException.
     * It catches the exception thrown when a requested resource is not found.
     *
     * @param ex The NotFoundException that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with a NOT_FOUND status code and a message from the exception.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * This method handles the NotModifiedException.
     * It catches the exception thrown when a resource has not been modified since the last request.
     *
     * @param ex The NotModifiedException that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with a NOT_MODIFIED status code and a message from the exception.
     */
    @ExceptionHandler(NotModifiedException.class)
    public ResponseEntity<ErrorResponse> handleNotModifiedException(NotModifiedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_MODIFIED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(errorResponse);
    }

    /**
     * This method handles the ConstraintViolationException.
     * It catches the exception thrown when a request violates the validation constraints.
     *
     * @param ex The ConstraintViolationException that was thrown.
     * @return A ResponseEntity containing an ErrorResponse with a BAD_REQUEST status code and a message from the exception.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
