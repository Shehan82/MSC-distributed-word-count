package com.dc.coordinator.service;

import com.dc.coordinator.dto.ProposerRequestDTO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CoordinatorService {

    private DiscoveryClient discoveryClient;

    private String fileStr;

    Map<Integer, String> proposerPortWiseLetterRangeStr;

    List<ServiceInstance> instances;

    public CoordinatorService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public void sendAssignedLettersAndFileStrToProposers() {
        this.instances = discoveryClient.getInstances("PROPOSER-SERVICE");
        this.proposerPortWiseLetterRangeStr = this.distributeLetterRanges(instances);
        this.sendDataToProposers();
    }

    private void sendDataToProposers() {
        RestTemplate restTemplate = new RestTemplate();
        this.fileStr = this.readFileAsString();

        for (ServiceInstance instance : this.instances)
            restTemplate.postForObject("http://localhost:" + instance.getPort() + "/calculateWordCount", this.getProposerRequestDTO(instance), ProposerRequestDTO.class);

    }

    private ProposerRequestDTO getProposerRequestDTO(ServiceInstance instance) {
        ProposerRequestDTO proposerRequestDTO = new ProposerRequestDTO();
        Integer port = instance.getPort();
        proposerRequestDTO.setFileStr(this.fileStr);
        proposerRequestDTO.setLetterRangeStr(proposerPortWiseLetterRangeStr.get(port));

        return proposerRequestDTO;
    }


    private String readFileAsString() {
        try {
            Path path = Paths.get("/home/shehan.dassanayake@ITDOM/Documents/my docs/MSC/SEM 4 - 2025/DC/assignment.txt");
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + "/home/shehan.dassanayake@ITDOM/Documents/my docs/MSC/SEM 4 - 2025/DC/assignment.txt", e);
        }
    }

    private Map<Integer, String> distributeLetterRanges(List<ServiceInstance> serviceInstances) {
        List<String> alphabetLetters = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        Map<Integer, Integer> servicePortWiseLetterCount = new HashMap<>();
        Map<Integer, String> servicePortWiseLetterRangeStr = new HashMap<>();
        Integer noOfLetters = alphabetLetters.size();
        Integer noOfInstances = serviceInstances.size();
        Integer letterCountForEachService = noOfLetters / noOfInstances;
        int remainingLetterCount = noOfLetters % noOfInstances;

        serviceInstances.forEach(serviceInstance -> {
            servicePortWiseLetterCount.put(serviceInstance.getPort(), letterCountForEachService);
        });

        for (Integer servicePort : servicePortWiseLetterCount.keySet()) {
            if (remainingLetterCount > 0) {
                servicePortWiseLetterCount.put(servicePort, servicePortWiseLetterCount.get(servicePort) + 1);
                remainingLetterCount--;
            }
        }

        int letterIndex = -1;
        for (Integer servicePort : servicePortWiseLetterCount.keySet()) {
            letterIndex = letterIndex + 1;
            Integer letterCount = servicePortWiseLetterCount.get(servicePort);
            StringBuilder letterRangeStr = new StringBuilder();
            letterRangeStr.append(alphabetLetters.get(letterIndex));
            letterRangeStr.append("-");
            letterIndex = letterIndex + letterCount - 1;
            letterRangeStr.append(alphabetLetters.get(letterIndex));

            servicePortWiseLetterRangeStr.put(servicePort, letterRangeStr.toString());
        }

        return servicePortWiseLetterRangeStr;
    }
}
