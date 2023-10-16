package com.placide.k8skafkaavroaepccleanarchibsmicroscompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class K8sKafkaAvroAepcCleanArchiBsMicrosCompanyApplication {

	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroAepcCleanArchiBsMicrosCompanyApplication.class)
				.run(args);
	}

}
