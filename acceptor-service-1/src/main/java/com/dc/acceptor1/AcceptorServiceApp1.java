package com.dc.acceptor1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AcceptorServiceApp1 {
    public static void main(String[] args) {
        SpringApplication.run(AcceptorServiceApp1.class, args);
    }
}
