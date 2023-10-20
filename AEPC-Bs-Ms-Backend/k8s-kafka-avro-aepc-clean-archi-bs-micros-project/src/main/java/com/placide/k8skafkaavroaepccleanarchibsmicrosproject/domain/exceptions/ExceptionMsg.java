package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions;

public enum ExceptionMsg {
    PROJECT_ALREADY_EXISTS_EXCEPTION("Project Already Exists Exception"),
    PROJECT_FIELD_EMPTY_EXCEPTION("Project, One or more Fields Empty Exception"),
    PROJECT_NOT_FOUND_EXCEPTION("Project Not Found Exception"),
    PROJECT_UNKNOWN_PRIORITY_EXCEPTION("Project Priority Unknown Exception"),
    PROJECT_UNKNOWN_STATE_EXCEPTION("Project State Unknown Exception"),
    REMOTE_COMPANY_API_EXCEPTION("Remote Company API Unreachable Exception"),
    REMOTE_EMPLOYEE_API_EXCEPTION("Remote Employee API Unreachable Exception");
    private final String message;

    ExceptionMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
