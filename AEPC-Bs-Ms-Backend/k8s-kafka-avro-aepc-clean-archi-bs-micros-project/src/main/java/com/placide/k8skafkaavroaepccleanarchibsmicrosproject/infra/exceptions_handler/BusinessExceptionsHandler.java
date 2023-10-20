package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.infra.exceptions_handler;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionsHandler {
    @ExceptionHandler(value = ProjectAlreadyExistsException.class)
    public ResponseEntity<Object> handleProjectAlreadyExistsException(){
        return new ResponseEntity<>(ExceptionMsg.PROJECT_ALREADY_EXISTS_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectPriorityInvalidException.class)
    public ResponseEntity<Object> handleProjectPriorityInvalidException(){
        return new ResponseEntity<>(ExceptionMsg.PROJECT_UNKNOWN_PRIORITY_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectStateInvalidException.class)
    public ResponseEntity<Object> handleProjectStateInvalidException(){
        return new ResponseEntity<>(ExceptionMsg.PROJECT_UNKNOWN_STATE_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectFieldsEmptyException.class)
    public ResponseEntity<Object> handleProjectFieldsEmptyException(){
        return new ResponseEntity<>(ExceptionMsg.PROJECT_FIELD_EMPTY_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = RemoteEmployeeApiException.class)
    public ResponseEntity<Object> handleRemoteEmployeeApiException(RemoteEmployeeApiException exception){
        return new ResponseEntity<>(exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = RemoteCompanyApiException.class)
    public ResponseEntity<Object> handleRemoteCompanyApiException(RemoteCompanyApiException exception){
        return new ResponseEntity<>(exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(){
        return new ResponseEntity<>(ExceptionMsg.PROJECT_NOT_FOUND_EXCEPTION.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
