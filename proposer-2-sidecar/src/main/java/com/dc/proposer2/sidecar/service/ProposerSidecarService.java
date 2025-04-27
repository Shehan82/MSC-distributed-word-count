package com.dc.proposer2.sidecar.service;

import com.dc.proposer2.sidecar.dto.ProposerFailureDTO;
import com.dc.proposer2.sidecar.dto.ProposerRequestDTO;
import com.dc.proposer2.sidecar.exception.CallNotPermittedRabbitMQException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProposerSidecarService {

    private static final Logger LOG = LoggerFactory.getLogger(ProposerSidecarService.class);

    @Value("${server.port}")
    private int port;

    @Value("${proposer.port}")
    private int proposerPort;

    @CircuitBreaker(name = "proposerService", fallbackMethod = "circuitBreakerFallBack")
    public void calculateWordCount(String line) {
        LOG.info("START: Proposer request to port : {} for proposer port : {}", port, proposerPort);
        ProposerRequestDTO proposerRequestDTO = new ProposerRequestDTO();
        proposerRequestDTO.setFileStr(line);
        proposerRequestDTO.setLineID(UUID.randomUUID().toString());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:" + proposerPort + "/calculateWordCount", proposerRequestDTO, ProposerRequestDTO.class);

        LOG.info("END: Proposer request to port : {} for proposer port : {}", port, proposerPort);
    }

    public void circuitBreakerFallBack(String input, Throwable t) throws IOException {
        LOG.info("FALLBACK TRIGGERED!!!");
        throw new CallNotPermittedRabbitMQException("Exception triggered during fallback call");
    }

    public void notifyCoordinatorOfProposerFailure() {
        RestTemplate restTemplate = new RestTemplate();
        ProposerFailureDTO proposerFailureDTO = new ProposerFailureDTO();
        proposerFailureDTO.setFailedNodePort(proposerPort);
        int coordinatorPort = 8082;
        restTemplate.postForObject("http://localhost:" + coordinatorPort + "/notifyProposerFailure", proposerFailureDTO, String.class);
    }
}
