package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.infra.adatpters.output.models;

public class AddressDto {
    private int num;
    private String street;
    private int poBox;
    private String city;
    private String country;

    public AddressDto() {
    }

    public AddressDto(int num, String street, int poBox, String city, String country) {
        this.num = num;
        this.street = street;
        this.poBox = poBox;
        this.city = city;
        this.country = country;
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
        return "AddressDto[" +
                "num=" + num +
                ", street='" + street + '\'' +
                ", poBox=" + poBox +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ']';
    }
}
