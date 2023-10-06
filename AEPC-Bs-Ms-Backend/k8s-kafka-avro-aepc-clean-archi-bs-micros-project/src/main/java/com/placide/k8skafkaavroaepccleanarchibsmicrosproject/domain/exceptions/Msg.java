package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.exceptions;

public enum Msg {
    PROJECT_ALREADY_EXISTS_EXCEPTION("Project Already Exists Exception"),
    PROJECT_FIELD_EMPTY_EXCEPTION("Project, One or more Fields Empty Exception"),
    PROJECT_UNKNOWN_PRIORITY_EXCEPTION("Project Priority Unknown Exception"),
    PROJECT_UNKNOWN_STATE_EXCEPTION("Project State Unknown Exception"),
    REMOTE_EMPLOYEE_API_EXCEPTION("Remote Employee API Exception"),
    REMOTE_COMPANY_API_EXCEPTION("Remote Company API Exception"),
    PROJECT_NOT_FOUND_EXCEPTION("Project Not Found Exception");
    private final String message;
    public static final String REMOTE_EMPLOYEE_API_UNREACHABLE ="Remote Employee API Unreachable";
    public static final String REMOTE_COMPANY_API_UNREACHABLE ="Remote Company API Unreachable";
    Msg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
