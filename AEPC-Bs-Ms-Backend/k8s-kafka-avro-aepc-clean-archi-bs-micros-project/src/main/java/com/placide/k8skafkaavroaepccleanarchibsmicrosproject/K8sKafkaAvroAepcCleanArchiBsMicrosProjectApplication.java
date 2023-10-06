package com.placide.k8skafkaavroaepccleanarchibsmicrosproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class K8sKafkaAvroAepcCleanArchiBsMicrosProjectApplication {

	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroAepcCleanArchiBsMicrosProjectApplication.class)
				.run(args);
	}

}
