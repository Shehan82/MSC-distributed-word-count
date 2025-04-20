package com.dc.learner.service;

import com.dc.learner.dto.AcceptorResponseDTO;
import com.dc.learner.dto.LearnerResultDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LearnerService {

    private Map<Integer, Map<Integer, AcceptorResponseDTO>> proposerPortWiseAcceptorPortWiseAcceptorResponseDTO = new ConcurrentHashMap<>();

    public void reconcileAcceptorResponses(AcceptorResponseDTO acceptorResponseDTO) {
        proposerPortWiseAcceptorPortWiseAcceptorResponseDTO.computeIfAbsent(acceptorResponseDTO.getProposerPort(), k -> new ConcurrentHashMap<>());
        proposerPortWiseAcceptorPortWiseAcceptorResponseDTO.get(acceptorResponseDTO.getProposerPort()).put(acceptorResponseDTO.getAcceptorPort(), acceptorResponseDTO);
    }

    public List<LearnerResultDTO> getLetterWiseWords() {

        List<LearnerResultDTO> result = new ArrayList<>();

        for (Integer proposerPort : proposerPortWiseAcceptorPortWiseAcceptorResponseDTO.keySet()) {

            Map<Integer, AcceptorResponseDTO> acceptorPortWiseAcceptorResponseDTO = proposerPortWiseAcceptorPortWiseAcceptorResponseDTO.get(proposerPort);
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
                Map<String, List<String>> letterWiseWords = validAcceptorResponseDTO.getLetterWiseWords();

                for (String letter : letterWiseWords.keySet()) {
                    LearnerResultDTO learnerResultDTO = new LearnerResultDTO();
                    learnerResultDTO.setLetter(letter);
                    learnerResultDTO.setWordCount(letterWiseWords.get(letter).size());

                    for (String word : letterWiseWords.get(letter)) {
                        learnerResultDTO.getWords().add(word);
                    }

                    result.add(learnerResultDTO);
                }

            } else {
                // TODO
            }
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
