package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions;


public enum ExceptionMessage {
    COMPANY_NOT_FOUND_EXCEPTION ("Company Not Found Exception"),
    COMPANY_FIELDS_EMPTY_EXCEPTION("Company One or more Fields Empty Exception"),
    COMPANY_ALREADY_EXISTS_EXCEPTION("Company Already Exists Exception"),
    COMPANY_TYPE_UNKNOWN_EXCEPTION("Company Type Unknown Exception");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
