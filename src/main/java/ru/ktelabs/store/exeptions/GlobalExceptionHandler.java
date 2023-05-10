package ru.ktelabs.store.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ktelabs.store.dtos.responses.errors.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ClientNotFoundException.class,
            ProductNotFoundException.class,
            StatisticNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({TotalCostDoesNotMatch.class,
            MoreThanOneArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .reasonPhrase(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .errorMessage(message)
                .build(), httpStatus);
    }
}
