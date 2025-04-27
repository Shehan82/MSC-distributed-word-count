package com.dc.proposer1.controller;

import com.dc.proposer1.dto.ProposerRequestDTO;
import com.dc.proposer1.service.ProposerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProposerController {

    private final ProposerService proposerService;

    public ProposerController(ProposerService proposerService) {
        this.proposerService = proposerService;
    }

    @PostMapping("/calculateWordCount")
    public void calculateWordCount(@RequestBody ProposerRequestDTO proposerRequestDTO) {
        this.proposerService.calculateWordCount(proposerRequestDTO);
    }

    @GetMapping("/updateLetterRanges")
    public void updateLetterRanges() {
        this.proposerService.assignLetterRanges();
    }
}
