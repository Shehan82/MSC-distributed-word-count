package com.dc.acceptor1.service;

import com.dc.acceptor1.dto.AcceptorRequestDTO;
import com.dc.acceptor1.dto.AcceptorResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AcceptorService {

    @Value("${server.port}")
    private Integer port;

    private final DiscoveryClient discoveryClient;

    public AcceptorService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public void validateWordCount(AcceptorRequestDTO acceptorRequestDTO) {

        Map<String, List<String>> letterWiseWords = new HashMap<>();
        int proposerWordCount = acceptorRequestDTO.getWordCount();
        int acceptorWordCount = 0;
        boolean isAcceptorAcceptWordCount = false;


        String[] lineWords = acceptorRequestDTO.getFileStr().split("[ \\n\\r\\t]+");

        for (String word : lineWords) {
            String upperCaseWord = word.toUpperCase();
            for (String letter : acceptorRequestDTO.getEligibleLetters()) {
                if (upperCaseWord.startsWith(letter)) {
                    letterWiseWords.computeIfAbsent(letter, k -> new ArrayList<>());
                    letterWiseWords.get(letter).add(word);
                }
            }
        }

        for (String letter : letterWiseWords.keySet()) {
            List<String> words = letterWiseWords.get(letter);
            acceptorWordCount += words.size();
        }


        isAcceptorAcceptWordCount = (acceptorWordCount == proposerWordCount);

        List<ServiceInstance> instances = discoveryClient.getInstances("LEARNER-SERVICE");
        ServiceInstance serviceInstance = instances.get(0);
        RestTemplate restTemplate = new RestTemplate();

        AcceptorResponseDTO acceptorResponseDTO = new AcceptorResponseDTO();
        acceptorResponseDTO.setAcceptorPort(this.port);
        acceptorResponseDTO.setProposerPort(acceptorRequestDTO.getProposerPort());
        acceptorResponseDTO.setLineID(acceptorRequestDTO.getLineID());
        acceptorResponseDTO.setLetterWiseWords(letterWiseWords);
        acceptorResponseDTO.setProposerLetterRanges(acceptorRequestDTO.getLetterRanges());
        acceptorResponseDTO.setAcceptorWordCount(acceptorWordCount);
        acceptorResponseDTO.setProposerWordCount(proposerWordCount);
        acceptorResponseDTO.setAcceptorAccepted(isAcceptorAcceptWordCount);


        int lernerPort = serviceInstance.getPort();
        restTemplate.postForObject("http://localhost:" + lernerPort + "/submitToLearner", acceptorResponseDTO, AcceptorResponseDTO.class);

    }

}
