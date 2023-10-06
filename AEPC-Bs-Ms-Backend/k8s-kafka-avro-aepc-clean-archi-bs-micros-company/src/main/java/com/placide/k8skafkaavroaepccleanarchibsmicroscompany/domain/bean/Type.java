package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.bean;

public enum Type {
    CLIENT("client"),
    PROSPECT("prospect"),
    ESN("esn");

    private final String companyType;

    Type(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyType() {
        return companyType;
    }
}
