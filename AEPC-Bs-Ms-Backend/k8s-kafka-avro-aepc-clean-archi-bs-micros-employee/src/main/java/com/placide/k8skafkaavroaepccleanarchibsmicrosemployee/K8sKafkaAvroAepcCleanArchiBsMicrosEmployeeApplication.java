package com.placide.k8skafkaavroaepccleanarchibsmicrosemployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class K8sKafkaAvroAepcCleanArchiBsMicrosEmployeeApplication {

	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroAepcCleanArchiBsMicrosEmployeeApplication.class)
				.run(args);
	}

}
