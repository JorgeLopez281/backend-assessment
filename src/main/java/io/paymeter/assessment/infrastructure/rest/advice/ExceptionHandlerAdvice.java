package io.paymeter.assessment.infrastructure.rest.advice;

import io.paymeter.assessment.domain.model.response.ErrorResponse;
import io.paymeter.assessment.infrastructure.adapter.exceptions.DurationException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.MoneyException;
import io.paymeter.assessment.infrastructure.adapter.exceptions.PricingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final String EXCEPTION_MESSAGE_CAUSE = "Exception Message {} Caused By {}";

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(PricingException.class)
    public ResponseEntity<ErrorResponse> handleEmptyInput(PricingException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.toString(),
                "Parking lot configuration is not found", ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DurationException.class)
    public ResponseEntity<ErrorResponse> handleDurationError(DurationException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Invalid duration: check-out date is earlier than check-in date.",
                ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MoneyException.class)
    public ResponseEntity<ErrorResponse> handleMoneyCalculationError(MoneyException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.CONFLICT.toString(),
                "Unable to calculate the parking price.",
                ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "error", fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message("Validation failed")
                .details(errors)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Invalid petition", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Internal Server Error", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Invalid or missing request body",
                "The request body is required and must be a valid JSON payload.", request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "Constraint Violation", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logException(ex);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.CONFLICT.toString(),
                "Data Integrity Violation", ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    private ErrorResponse buildErrorResponse(String code, String message, Object details, WebRequest request) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .details(details)
                .timestamp(LocalDateTime.now().toString())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
    }

    private void logException(Exception ex) {
        String cause = (ex.getCause() != null) ? ex.getCause().toString() : "N/A";
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), cause);
    }
}
