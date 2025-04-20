package com.dc.coordinator.controller;

import com.dc.coordinator.service.CoordinatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping("/assignLetterRangesAndDistribute")
    private void assignLetterRangesAndDistribute() {
        this.coordinatorService.sendAssignedLettersAndFileStrToProposers();
    }
}
