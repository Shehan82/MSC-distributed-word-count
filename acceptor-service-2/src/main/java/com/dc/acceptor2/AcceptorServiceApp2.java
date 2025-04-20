package com.dc.acceptor2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AcceptorServiceApp2 {
    public static void main(String[] args) {
        SpringApplication.run(AcceptorServiceApp2.class, args);
    }
}
