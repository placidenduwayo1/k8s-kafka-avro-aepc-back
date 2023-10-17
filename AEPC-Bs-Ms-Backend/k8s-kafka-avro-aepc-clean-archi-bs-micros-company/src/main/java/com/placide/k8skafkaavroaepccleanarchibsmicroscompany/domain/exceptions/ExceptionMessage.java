package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.exceptions;


public enum ExceptionMessage {
    COMPANY_NOT_FOUND_EXCEPTION ("Company Not Found Exception"),
    COMPANY_FIELDS_EMPTY_EXCEPTION("Company One or more Fields Empty Exception"),
    COMPANY_ALREADY_EXISTS_EXCEPTION("Company Already Exists Exception"),
    COMPANY_TYPE_UNKNOWN_EXCEPTION("Company Type Unknown Exception"),
    REMOTE_ADDRESS_API_EXCEPTION("Remote Address Api Unreachable Exception"),
    ADDRESS_API_UNREACHABLE ("address api unreachable");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
