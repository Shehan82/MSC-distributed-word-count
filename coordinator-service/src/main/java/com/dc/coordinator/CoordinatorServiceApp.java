package com.dc.coordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CoordinatorServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(CoordinatorServiceApp.class, args);
    }
}
