package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.employee;

import com.placide.k8skafkaavroaepccleanarchibsmicrosemployee.domain.beans.address.Address;

public class Employee {
    private String employeeId;
    private String firstname;
    private String lastname;
    private String email;
    private String hireDate;
    private String state;
    private String type;
    private String addressId;
    private Address address;

    public Employee() {
    }

    public Employee(String employeeId, String firstname,
                    String lastname, String email, String hireDate,
                    String state, String type,
                    String addressId, Address address) {
        this.employeeId = employeeId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.hireDate = hireDate;
        this.state = state;
        this.type = type;
        this.addressId = addressId;
        this.address = address;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeID) {
        this.employeeId = employeeID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Employee [" +
                "id:'" + employeeId + '\'' +
                ", firstname:'" + firstname + '\'' +
                ", lastname:'" + lastname + '\'' +
                ", email:'" + email + '\'' +
                ", hire-date:'" + hireDate + '\'' +
                ", state:'" + state + '\'' +
                ", type:'" + type + '\'' +
                ", address:" + address +
                ']';
    }
}
