package com.dc.proposer2.service;

import com.dc.proposer2.dao.ProposerDao;
import com.dc.proposer2.dto.AcceptorRequestDTO;
import com.dc.proposer2.dto.ProposerRequestDTO;
import com.dc.proposer2.model.Proposer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProposerService {

    @Value("${server.port}")
    private int port;

    private final DiscoveryClient discoveryClient;

    private final ProposerDao proposerDao;

    private List<String> letterRanges;

    public ProposerService(DiscoveryClient discoveryClient, ProposerDao proposerDao) {
        this.discoveryClient = discoveryClient;
        this.proposerDao = proposerDao;
    }

    public void assignLetterRanges() {
        if (this.letterRanges == null) {
            List<Proposer> proposers = proposerDao.findByPortAndStatus(port, "ACT");
            proposers.forEach(proposer -> {
                this.letterRanges = new ArrayList<>();
                this.letterRanges.add(proposer.getLetterRange());
            });
        }

    }

    public void calculateWordCount(ProposerRequestDTO proposerRequestDTO) {
        this.assignLetterRanges();
        List<String> alphabetLetters = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");


        Set<String> eligibleLetters = new HashSet<>();

        for (String letterRange : letterRanges) {
            List<String> letters = this.getLettersFromLetterRangeString(letterRange, alphabetLetters);
            eligibleLetters.addAll(letters);
        }


        AcceptorRequestDTO acceptorRequestDTO = new AcceptorRequestDTO();

        String[] words = proposerRequestDTO.getFileStr().split("[ \\n\\r\\t]+");


        int eligibleWordCount = 0;

        for (String word : words) {
            String upperCaseWord = word.toUpperCase();
            for (String letter : eligibleLetters) {
                if (upperCaseWord.startsWith(letter))
                    eligibleWordCount++;
            }
        }


        acceptorRequestDTO.setLetterRanges(letterRanges);
        acceptorRequestDTO.setFileStr(proposerRequestDTO.getFileStr());
        acceptorRequestDTO.setWordCount(eligibleWordCount);
        acceptorRequestDTO.setProposerPort(port);
        acceptorRequestDTO.setEligibleLetters(eligibleLetters);
        acceptorRequestDTO.setLineID(proposerRequestDTO.getLineID());

        List<ServiceInstance> instances = discoveryClient.getInstances("ACCEPTOR-SERVICE");
        RestTemplate restTemplate = new RestTemplate();

        for (ServiceInstance instance : instances) {
            int port = instance.getPort();
            restTemplate.postForObject("http://localhost:" + port + "/validateWordCount", acceptorRequestDTO, AcceptorRequestDTO.class);
        }
    }


    private List<String> getLettersFromLetterRangeString(String letterRangeStr, List<String> alphabetLetters) {
        List<String> result = new ArrayList<>();
        String[] letters = letterRangeStr.split("-");
        String startLetter = letters[0];
        String endLetter = letters[1];

        boolean startLetterFound = false;
        boolean endLetterFound = false;

        for (String letter : alphabetLetters) {

            if (!startLetterFound) {
                startLetterFound = startLetter.equals(letter);
            }

            if (startLetterFound && !endLetterFound) {
                result.add(letter);
            }

            if (!endLetterFound) {
                endLetterFound = endLetter.equals(letter);
            }
        }
        return result;
    }
}
