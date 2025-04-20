package com.dc.proposer3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProposerServiceApp3 {
    public static void main(String[] args) {
        SpringApplication.run(ProposerServiceApp3.class, args);
    }
}
