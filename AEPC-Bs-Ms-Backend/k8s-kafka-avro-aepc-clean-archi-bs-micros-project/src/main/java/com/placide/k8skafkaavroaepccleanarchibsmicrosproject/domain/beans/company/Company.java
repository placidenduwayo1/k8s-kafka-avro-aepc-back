package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.company;

public class Company {
    private String companyId;
    private String name;
    private String agency;
    private String type;
    private String connectedDate;

    public Company() {
    }

    public Company(String companyId, String name, String agency, String type, String connectedDate) {
        this.companyId = companyId;
        this.name = name;
        this.agency = agency;
        this.type = type;
        this.connectedDate = connectedDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConnectedDate() {
        return connectedDate;
    }

    public void setConnectedDate(String connectedDate) {
        this.connectedDate = connectedDate;
    }

    @Override
    public String toString() {
        return "Company[" +
                "id='" + companyId + '\'' +
                ", name='" + name + '\'' +
                ", agency='" + agency + '\'' +
                ", type='" + type + '\'' +
                ", connectedDate=" + connectedDate +
                ']';
    }
}
