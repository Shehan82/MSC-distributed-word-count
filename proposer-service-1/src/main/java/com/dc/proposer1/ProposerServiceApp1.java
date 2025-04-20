package com.dc.proposer1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProposerServiceApp1 {
    public static void main(String[] args) {
        SpringApplication.run(ProposerServiceApp1.class, args);
    }
}
