package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee;

public enum State {
    ACTIVE("active"),
    ARCHIVE("archive");

    private final String employeeState;

    public String getEmployeeState() {
        return employeeState;
    }
    State(String employeeState) {
        this.employeeState = employeeState;
    }
}
