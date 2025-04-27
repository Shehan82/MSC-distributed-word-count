package com.dc.proposer2.service;

import com.dc.proposer2.dto.ProposerStarterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StartupRunner implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Server started on port " + port);

        RestTemplate restTemplate = new RestTemplate();
        int coordinatorPort = 8082;
        ProposerStarterDTO proposerStarterDTO = new ProposerStarterDTO();
        proposerStarterDTO.setProposerPort(port);
        restTemplate.postForObject("http://localhost:" + coordinatorPort + "/notifyProposerStarter", proposerStarterDTO, String.class);
    }
}
