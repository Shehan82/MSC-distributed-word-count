package com.dc.acceptor3.service;

import com.dc.acceptor3.dto.AcceptorRequestDTO;
import com.dc.acceptor3.dto.AcceptorResponseDTO;
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


        String[] fileWords = acceptorRequestDTO.getFileStr().split("[ \\n\\r\\t]+");

        for (String word : fileWords) {
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
        acceptorResponseDTO.setLetterWiseWords(letterWiseWords);
        acceptorResponseDTO.setProposerLetterRangeStr(acceptorRequestDTO.getLetterRangeStr());
        acceptorResponseDTO.setAcceptorWordCount(acceptorWordCount);
        acceptorResponseDTO.setProposerWordCount(proposerWordCount);
        acceptorResponseDTO.setAcceptorAccepted(isAcceptorAcceptWordCount);


        int lernerPort = serviceInstance.getPort();
        restTemplate.postForObject("http://localhost:" + lernerPort + "/submitToLearner", acceptorResponseDTO, AcceptorResponseDTO.class);

    }

}
