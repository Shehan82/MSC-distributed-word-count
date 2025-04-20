package com.dc.acceptor3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AcceptorServiceApp3 {
    public static void main(String[] args) {
        SpringApplication.run(AcceptorServiceApp3.class, args);
    }
}
