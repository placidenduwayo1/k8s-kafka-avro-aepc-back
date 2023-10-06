package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project;

import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company.Company;
import com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.employee.Employee;

public class Project {
    private String projectId;
    private String name;
    private String description;
    private int priority;
    private String state;
    private String createdDate;
    private String employeeId;
    private Employee employee;
    private String companyId;
    private Company company;

    public Project() {
    }

    public Project(String projectId, String name, String description, int priority, String state, String createdDate,
                   String employeeId, Employee employee, String companyId, Company company) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.state = state;
        this.createdDate = createdDate;
        this.employeeId = employeeId;
        this.employee = employee;
        this.companyId = companyId;
        this.company = company;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Project[" +
                "uuid='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", state='" + state + '\'' +
                ", createdDate=" + createdDate +
                ", employeeId='" + employeeId + '\'' +
                ", employee=" + employee +
                ", companyId='" + companyId + '\'' +
                ", company=" + company +
                ']';
    }
}
