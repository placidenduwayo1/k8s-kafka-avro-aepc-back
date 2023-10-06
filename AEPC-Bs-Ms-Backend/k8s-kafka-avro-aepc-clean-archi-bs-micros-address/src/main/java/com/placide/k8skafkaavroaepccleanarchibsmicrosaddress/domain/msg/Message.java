package com.placide.k8skafkaavroaepccleanarchibsmicrosaddress.domain.msg;

public enum Message {
    ADDRESS_ALREADY_EXISTS_EXCEPTION("Address Already Exists Exception"),
    ADDRESS_FIELDS_INVALID_EXCEPTION("Address Fields Invalid Exception"),
    ADDRESS_NOT_FOUND_EXCEPTION("Address Not Found Exception"),
    ADDRESS_CITY_NOT_EXCEPTION("Address City Not Found Exception");
    private final String msg;

    Message(String message) {
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }
}
