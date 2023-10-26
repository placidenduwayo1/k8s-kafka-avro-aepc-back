package com.placide.k8skafkaavrogatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class K8sKafkaAvroGatewayServiceApplication {
	public static void main(String[] args) {
		new SpringApplication(K8sKafkaAvroGatewayServiceApplication.class)
				.run(args);
	}
	@Bean
	public DiscoveryClientRouteDefinitionLocator discoverClientRoute(ReactiveDiscoveryClient rdc,
																	 DiscoveryLocatorProperties dlp){
		return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
	}
}
