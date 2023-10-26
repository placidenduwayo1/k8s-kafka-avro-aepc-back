package com.placide.k8skafkaavrogatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class K8sKafkaAvroGatewayServiceApplication {
	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroGatewayServiceApplication.class)
				.run(args);
	}
}
