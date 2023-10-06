package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.exceptions;

public enum ExceptionsMsg {
    EMPLOYEE_ALREADY_EXISTS_EXCEPTION("Employee Already Exists Exception"),
    EMPLOYEE_NOT_FOUND_EXCEPTION("Employee Not Found Exception"),
    EMPLOYEE_FIELDS_EMPTY_EXCEPTION("Employee One or More Fields Empty Exception"),
    REMOTE_ADDRESS_API_EXCEPTION("Remote Address Api Unreachable Exception"),
    EMPLOYEE_UNKNOWN_STATE_EXCEPTION("Employee State Unknown Exception"),
    EMPLOYEE_UNKNOWN_TYPE_EXCEPTION("Employee Type Unknown Exception");

    private final String message;
    public static final String ADDRESS_API_UNREACHABLE ="address api unreachable";

    ExceptionsMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
