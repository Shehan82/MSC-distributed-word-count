package com.dc.proposer2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProposerServiceApp2 {
    public static void main(String[] args) {
        SpringApplication.run(ProposerServiceApp2.class, args);
    }
}
