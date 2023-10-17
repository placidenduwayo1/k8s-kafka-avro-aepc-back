package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.exceptions_handler;

import com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CompanyBusinessExceptionHandler {
    @ExceptionHandler(value = CompanyNotFoundException.class)
    public ResponseEntity<Object> handleCompanyNotFoundException(){
        return new ResponseEntity<>(ExceptionMessage.COMPANY_NOT_FOUND_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = CompanyEmptyFieldsException.class)
    public ResponseEntity<Object> handleCompanyEmptyFieldsException(){
        return new ResponseEntity<>(ExceptionMessage.COMPANY_FIELDS_EMPTY_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = CompanyAlreadyExistsException.class)
    public ResponseEntity<Object> handleCompanyAlreadyExistsException(){
        return new ResponseEntity<>(ExceptionMessage.COMPANY_ALREADY_EXISTS_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = CompanyTypeInvalidException.class)
    public ResponseEntity<Object> handleCompanyTypeInvalidException(){
        return new ResponseEntity<>(ExceptionMessage.COMPANY_TYPE_UNKNOWN_EXCEPTION.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = RemoteApiAddressNotLoadedException.class)
    public ResponseEntity<Object> handleRemoteApiAddressNotLoadedException(){
        return new ResponseEntity<>(ExceptionMessage.REMOTE_ADDRESS_API_EXCEPTION
                .getMessage(),
                HttpStatus.NOT_ACCEPTABLE);
    }
}
