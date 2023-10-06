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
        return new ResponseEntity<>(Msg.PROJECT_ALREADY_EXISTS_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectPriorityInvalidException.class)
    public ResponseEntity<Object> handleProjectPriorityInvalidException(){
        return new ResponseEntity<>(Msg.PROJECT_UNKNOWN_PRIORITY_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectStateInvalidException.class)
    public ResponseEntity<Object> handleProjectStateInvalidException(){
        return new ResponseEntity<>(Msg.PROJECT_UNKNOWN_STATE_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectFieldsEmptyException.class)
    public ResponseEntity<Object> handleProjectFieldsEmptyException(){
        return new ResponseEntity<>(Msg.PROJECT_FIELD_EMPTY_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = RemoteEmployeeApiException.class)
    public ResponseEntity<Object> handleRemoteEmployeeApiException(){
        return new ResponseEntity<>(Msg.REMOTE_EMPLOYEE_API_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = RemoteCompanyApiException.class)
    public ResponseEntity<Object> handleRemoteCompanyApiException(){
        return new ResponseEntity<>(Msg.REMOTE_COMPANY_API_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(){
        return new ResponseEntity<>(Msg.PROJECT_NOT_FOUND_EXCEPTION
                .getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
