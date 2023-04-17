package hr.bank.creditcardregistryservice.exception.handler;

import hr.bank.creditcardregistryservice.exception.ClientAlreadyExistsException;
import hr.bank.creditcardregistryservice.exception.handler.error.ApiError;
import hr.bank.creditcardregistryservice.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        return getResponseEntity(status, errors, ex);
    }


    @ExceptionHandler(value = {ClientAlreadyExistsException.class})
    protected ResponseEntity<Object> handleClientAlreadyExistsException(Exception ex, WebRequest request) {
        logger.error("Client already exists:", ex);
        return getResponseEntity(HttpStatusCode.valueOf(500), ex);
    }

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<Object> handleIOException(Exception ex, WebRequest request) {
        logger.error("Error occurred while writing to disk:", ex);
        return getResponseEntity(HttpStatusCode.valueOf(500), ex);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleEntityNotFound(Exception ex, WebRequest request) {
        logger.warn("EntityNotFound:", ex);
        return getResponseEntity(HttpStatusCode.valueOf(404), ex);
    }

    private ResponseEntity<Object> getResponseEntity(HttpStatusCode status, Throwable ex) {
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add(ex.getMessage());
        return new ResponseEntity<>(
                new ApiError(errorMessages, HttpStatusCode.valueOf(404)), HttpStatusCode.valueOf(404)
        );
    }

    private ResponseEntity<Object> getResponseEntity(HttpStatusCode status, List<String> errorMessages, Throwable ex) {
        return new ResponseEntity<>(
                new ApiError(errorMessages, HttpStatusCode.valueOf(404)), HttpStatusCode.valueOf(404)
        );
    }

}