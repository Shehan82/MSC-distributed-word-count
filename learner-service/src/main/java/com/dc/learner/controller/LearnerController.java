package com.dc.learner.controller;

import com.dc.learner.dto.AcceptorResponseDTO;
import com.dc.learner.dto.LearnerResultDTO;
import com.dc.learner.service.LearnerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class LearnerController {

    private final LearnerService learnerService;

    public LearnerController(LearnerService learnerService) {
        this.learnerService = learnerService;
    }

    @PostMapping("/submitToLearner")
    public void submitToLearner(@RequestBody AcceptorResponseDTO acceptorResponseDTO) {
        this.learnerService.reconcileAcceptorResponses(acceptorResponseDTO);
    }

    @GetMapping("/getLearnerResult")
    public List<LearnerResultDTO> getLearnerResult() {
        return this.learnerService.getLetterWiseWords();
    }
}
