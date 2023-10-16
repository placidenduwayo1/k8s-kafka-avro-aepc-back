package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.domain.beans.address;

public class Address {
    private String addressId;
    private int num;
    private String street;
    private int poBox;
    private String city;
    private String country;

    public Address() {
    }

    public Address(String addressId, int num, String street, int poBox,
                   String city, String country) {
        this.addressId = addressId;
        this.num = num;
        this.street = street;
        this.poBox = poBox;
        this.city = city;
        this.country = country;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPoBox() {
        return poBox;
    }

    public void setPoBox(int poBox) {
        this.poBox = poBox;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "address-api [" +
                "id:'" + addressId + '\'' +
                ", num:" + num +
                ", street:'" + street + '\'' +
                ", po-box:" + poBox +
                ", city:'" + city + '\'' +
                ", country:'" + country + '\'' +
                ']';
    }
}
