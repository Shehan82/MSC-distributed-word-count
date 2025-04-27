package com.dc.acceptor1.dto;

import java.util.List;
import java.util.Map;

public class AcceptorResponseDTO {

    private Integer proposerPort;

    private Integer acceptorPort;

    private List<String> proposerLetterRanges;

    private String lineID;

    private Map<String, List<String>> letterWiseWords;

    private Integer acceptorWordCount;

    private Integer proposerWordCount;

    private boolean isAcceptorAccepted;

    public Integer getProposerPort() {
        return proposerPort;
    }

    public void setProposerPort(Integer proposerPort) {
        this.proposerPort = proposerPort;
    }

    public Integer getAcceptorPort() {
        return acceptorPort;
    }

    public void setAcceptorPort(Integer acceptorPort) {
        this.acceptorPort = acceptorPort;
    }

    public Map<String, List<String>> getLetterWiseWords() {
        return letterWiseWords;
    }

    public void setLetterWiseWords(Map<String, List<String>> letterWiseWords) {
        this.letterWiseWords = letterWiseWords;
    }

    public Integer getAcceptorWordCount() {
        return acceptorWordCount;
    }

    public void setAcceptorWordCount(Integer acceptorWordCount) {
        this.acceptorWordCount = acceptorWordCount;
    }

    public Integer getProposerWordCount() {
        return proposerWordCount;
    }

    public void setProposerWordCount(Integer proposerWordCount) {
        this.proposerWordCount = proposerWordCount;
    }

    public boolean isAcceptorAccepted() {
        return isAcceptorAccepted;
    }

    public void setAcceptorAccepted(boolean acceptorAccepted) {
        isAcceptorAccepted = acceptorAccepted;
    }

    public List<String> getProposerLetterRanges() {
        return proposerLetterRanges;
    }

    public void setProposerLetterRanges(List<String> proposerLetterRanges) {
        this.proposerLetterRanges = proposerLetterRanges;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }
}
