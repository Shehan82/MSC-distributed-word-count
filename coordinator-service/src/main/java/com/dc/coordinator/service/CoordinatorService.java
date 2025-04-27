package com.dc.coordinator.service;

import com.dc.coordinator.config.ProducerConfig;
import com.dc.coordinator.dao.ProposerDao;
import com.dc.coordinator.dto.*;
import com.dc.coordinator.model.Proposer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CoordinatorService {

    private final DiscoveryClient discoveryClient;

    private final RabbitTemplate rabbitTemplate;

    private final ProposerDao proposerDao;

    private String fileStr;

    Map<Integer, String> proposerPortWiseLetterRangeStr;

    List<ServiceInstance> instances;

    public CoordinatorService(DiscoveryClient discoveryClient, RabbitTemplate rabbitTemplate, ProposerDao proposerDao) {
        this.discoveryClient = discoveryClient;
        this.rabbitTemplate = rabbitTemplate;
        this.proposerDao = proposerDao;
    }

    public void sendAssignedLettersAndFileStrToProposers() {
        this.instances = discoveryClient.getInstances("PROPOSER-SERVICE");
        this.proposerPortWiseLetterRangeStr = this.distributeLetterRanges(instances);

        this.saveProposers();
        this.sendDataToProposers();
    }

    private void saveProposers() {
        this.proposerDao.deleteAll();

        List<Proposer> proposers = new ArrayList<>();
        for (Integer proposerPort : this.proposerPortWiseLetterRangeStr.keySet()) {
            Proposer proposer = new Proposer();
            proposer.setPort(proposerPort);
            proposer.setLetterRange(this.proposerPortWiseLetterRangeStr.get(proposerPort));
            proposer.setStatus("ACT");

            proposers.add(proposer);
        }

        this.proposerDao.saveAll(proposers);
    }

    private void sendDataToProposers() {
        RestTemplate restTemplate = new RestTemplate();
//        this.fileStr = this.readFileAsString();

        String filePath = "/home/shehan.dassanayake@ITDOM/Documents/my docs/MSC/SEM 4 - 2025/DC/assignment.txt"; // Replace with your actual file path

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
//                System.out.println("Line: " + line);
                rabbitTemplate.convertAndSend(ProducerConfig.LINE_EXCHANGE, "", line);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

//        for (ServiceInstance instance : this.instances)
//            restTemplate.postForObject("http://localhost:" + instance.getPort() + "/calculateWordCount", this.getProposerRequestDTO(instance), ProposerRequestDTO.class);

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

        if (noOfInstances != 0) {
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
        }

        return servicePortWiseLetterRangeStr;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void assignFailedNodeLetterRangesToNewNode(ProposerFailureDTO proposerFailureDTO) {
        List<Proposer> failedProposers = this.proposerDao.findByPortAndStatus(proposerFailureDTO.getFailedNodePort(), "ACT");

        if (failedProposers != null && !failedProposers.isEmpty()) {
            List<ServiceInstance> proposerInstances = this.discoveryClient.getInstances("PROPOSER-SERVICE");

            if (proposerInstances != null && !proposerInstances.isEmpty()) {
                ServiceInstance selectedProposer = null;
                List<String> failedNodeLetterRanges = new ArrayList<>();
                RestTemplate restTemplate = new RestTemplate();

                failedProposers.forEach(proposer -> {
                    proposer.setStatus("INA");
                    failedNodeLetterRanges.add(proposer.getLetterRange());
                });

                this.proposerDao.saveAll(failedProposers);

                if (!proposerInstances.isEmpty()) {
                    selectedProposer = proposerInstances.get(0);
                    LetterRangeAssignmentDTO letterRangeAssignmentDTO = new LetterRangeAssignmentDTO();
                    List<Proposer> newProposers = new ArrayList<>();
                    if (!failedNodeLetterRanges.isEmpty() && selectedProposer != null) {
                        for (String letterRange : failedNodeLetterRanges) {
                            Proposer proposer = new Proposer();
                            letterRangeAssignmentDTO.getLetterRanges().add(letterRange);
                            proposer.setPort(selectedProposer.getPort());
                            proposer.setLetterRange(letterRange);
                            proposer.setStatus("ACT");
                            newProposers.add(proposer);
                        }

                        this.saveProposers(newProposers);
                        restTemplate.getForObject("http://localhost:" + selectedProposer.getPort() + "/updateLetterRanges", String.class);
                    }
                }
            }


        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void reAssignProposerLetterRange(ProposerStarterDTO proposerStarterDTO) {
        List<Proposer> proposers = this.proposerDao.findByPortAndStatus(proposerStarterDTO.getProposerPort(), "INA");
        List<String> proposerLetterRanges = new ArrayList<>();
        List<Integer> prevAllocatedProposerPorts = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        if (proposers != null && !proposers.isEmpty()) {
            for (Proposer proposer : proposers) {
                proposer.setStatus("ACT");
                proposerLetterRanges.add(proposer.getLetterRange());
            }

            List<Proposer> previousAllocations = this.proposerDao.findByLetterRangeInAndStatus(proposerLetterRanges, "ACT");

            if (previousAllocations != null && !previousAllocations.isEmpty()) {
                previousAllocations.forEach(proposer -> {
                    proposer.setStatus("INA");
                    prevAllocatedProposerPorts.add(proposer.getPort());
                });

                this.saveProposers(previousAllocations);
                prevAllocatedProposerPorts.forEach(proposerPort -> {
                    restTemplate.getForObject("http://localhost:" + proposerPort + "/updateLetterRanges", String.class);
                });
            }

            this.saveProposers(proposers);
            restTemplate.getForObject("http://localhost:" + proposerStarterDTO.getProposerPort() + "/updateLetterRanges", String.class);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProposers(List<Proposer> proposers) {
        this.proposerDao.saveAllAndFlush(proposers);
    }
}
