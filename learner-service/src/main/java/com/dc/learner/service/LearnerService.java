package com.dc.learner.service;

import com.dc.learner.dto.AcceptorResponseDTO;
import com.dc.learner.dto.LearnerResultDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LearnerService {

    private Map<Integer, Map<String, Map<Integer, AcceptorResponseDTO>>> proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO = new ConcurrentHashMap<>();

    public void reconcileAcceptorResponses(AcceptorResponseDTO acceptorResponseDTO) {
        proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO.computeIfAbsent(acceptorResponseDTO.getProposerPort(), k -> new ConcurrentHashMap<>());
        proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO.get(acceptorResponseDTO.getProposerPort()).computeIfAbsent(acceptorResponseDTO.getLineID(), k -> new ConcurrentHashMap<>());
        proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO.get(acceptorResponseDTO.getProposerPort()).get(acceptorResponseDTO.getLineID()).put(acceptorResponseDTO.getAcceptorPort(), acceptorResponseDTO);
    }

    public List<LearnerResultDTO> getLetterWiseWords() {

        Map<String, List<String>> letterWiseWords = new HashMap<>();
        List<LearnerResultDTO> result = new ArrayList<>();


        for (Integer proposerPort : proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO.keySet()) {

            Map<String, Map<Integer, AcceptorResponseDTO>> lineIDWiseAcceptorPortWiseAcceptorResponseDTO = proposerPortWiseLineIDWiseAcceptorPortWiseAcceptorResponseDTO.get(proposerPort);

            for (String lineID : lineIDWiseAcceptorPortWiseAcceptorResponseDTO.keySet()) {
                Map<Integer, AcceptorResponseDTO> acceptorPortWiseAcceptorResponseDTO = lineIDWiseAcceptorPortWiseAcceptorResponseDTO.get(lineID);
                int totalAcceptorNodesCount = acceptorPortWiseAcceptorResponseDTO.size();
                double wordCountAcceptedAcceptorNodes = 0d;
                AcceptorResponseDTO validAcceptorResponseDTO = null;

                for (Integer acceptorPort : acceptorPortWiseAcceptorResponseDTO.keySet()) {
                    AcceptorResponseDTO acceptorResponseDTO = acceptorPortWiseAcceptorResponseDTO.get(acceptorPort);
                    if (acceptorResponseDTO.isAcceptorAccepted()) {
                        wordCountAcceptedAcceptorNodes += 1;
                        validAcceptorResponseDTO = acceptorResponseDTO;
                    }
                }

                if (wordCountAcceptedAcceptorNodes / totalAcceptorNodesCount >= 0.5 && validAcceptorResponseDTO != null) {
                    Map<String, List<String>> lineLetterWiseWords = validAcceptorResponseDTO.getLetterWiseWords();

                    for (String letter : lineLetterWiseWords.keySet()) {
                        for (String word : lineLetterWiseWords.get(letter)) {
                            letterWiseWords.computeIfAbsent(letter, k -> new ArrayList<>());
                            letterWiseWords.get(letter).add(word);
                        }
                    }

                } else {
                    // TODO
                }
            }


        }

        for (String letter : letterWiseWords.keySet()) {
            LearnerResultDTO learnerResultDTO = new LearnerResultDTO();
            learnerResultDTO.setLetter(letter);
            learnerResultDTO.setWordCount(letterWiseWords.get(letter).size());

            for (String word : letterWiseWords.get(letter)) {
                learnerResultDTO.getWords().add(word);
            }

            result.add(learnerResultDTO);
        }


        Comparator<LearnerResultDTO> learnerResultComparator = new Comparator<>() {
            @Override
            public int compare(LearnerResultDTO o1, LearnerResultDTO o2) {
                return o1.getLetter().compareTo(o2.getLetter());
            }
        };

        result.sort(learnerResultComparator);


        System.out.println();
        System.out.println("LETTER  : COUNT  :  WORDS");
        System.out.println();
        for (LearnerResultDTO learnerResultDTO : result) {
            StringBuilder wordsBuilder = new StringBuilder();
            int counter = 0;
            for (String word : learnerResultDTO.getWords()) {
                wordsBuilder.append(word);
                if (learnerResultDTO.getWords().size() - 1 != counter) {
                    wordsBuilder.append(", ");
                }
                counter++;
            }

            System.out.println(learnerResultDTO.getLetter() + "       :" + String.format("%5d", learnerResultDTO.getWordCount()) + "   :  " + wordsBuilder);
        }

        return result;
    }
}
