package com.placide.k8skafkaavrogatewayservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
    @Value("${message}")
    private String welcomeMsg;
    @GetMapping(value = "")
    public String getWelcomeMsg(){
        return welcomeMsg;
    }
}
