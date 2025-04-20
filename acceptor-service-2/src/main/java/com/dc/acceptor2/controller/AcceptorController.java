package com.dc.acceptor2.controller;

import com.dc.acceptor2.dto.AcceptorRequestDTO;
import com.dc.acceptor2.service.AcceptorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcceptorController {

    private AcceptorService acceptorService;

    public AcceptorController(AcceptorService acceptorService) {
        this.acceptorService = acceptorService;
    }

    @PostMapping("/validateWordCount")
    public void validateWordCount(@RequestBody AcceptorRequestDTO acceptorRequestDTO) {
        this.acceptorService.validateWordCount(acceptorRequestDTO);
    }
}
