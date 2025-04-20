package com.dc.proposer3.controller;

import com.dc.proposer3.dto.AcceptorRequestDTO;
import com.dc.proposer3.dto.ProposerRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProposerController {

    @Value("${server.port}")
    private int port;

    private final DiscoveryClient discoveryClient;

    public ProposerController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @PostMapping("/calculateWordCount")
    public void calculateWordCount(@RequestBody ProposerRequestDTO proposerRequestDTO) {
        List<String> alphabetLetters = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        List<String> eligibleLetters = this.getLettersFromLetterRangeString(proposerRequestDTO.getLetterRangeStr(), alphabetLetters);
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


        System.out.println(proposerRequestDTO.getLetterRangeStr());

        acceptorRequestDTO.setLetterRangeStr(proposerRequestDTO.getLetterRangeStr());
        acceptorRequestDTO.setFileStr(proposerRequestDTO.getFileStr());
        acceptorRequestDTO.setWordCount(eligibleWordCount);
        acceptorRequestDTO.setProposerPort(port);
        acceptorRequestDTO.setEligibleLetters(eligibleLetters);

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
