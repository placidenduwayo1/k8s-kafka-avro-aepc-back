package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee;

public enum Type {
    CTO("cto"),
    CEO("ceo"),
    HR("hr"),
    TECH("tech-manager"),
    COM("com-manager"),
    EMPL("employee"),
    TAR("talent-acquisition-referent"),
    SE("software-engineer"),
    BS("business-manager");
    private final String employeeType;

    Type(String employeeType) {
        this.employeeType = employeeType;
    }
    public String getEmployeeType() {
        return employeeType;
    }
}
