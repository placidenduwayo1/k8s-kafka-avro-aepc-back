package com.placide.k8skafkaavromicroservicesregistryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class K8sKafkaAvroMicroservicesRegistryServerApplication {

	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroMicroservicesRegistryServerApplication.class)
				.run(args);
	}

}
