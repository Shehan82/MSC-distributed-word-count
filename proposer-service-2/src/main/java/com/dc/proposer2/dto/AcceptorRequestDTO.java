package com.dc.proposer2.dto;

import java.util.List;
import java.util.Set;

public class AcceptorRequestDTO {

    private List<String> letterRanges;

    private int wordCount;

    private int proposerPort;

    private String fileStr;

    private Set<String> eligibleLetters;

    private String lineID;

    public List<String> getLetterRanges() {
        return letterRanges;
    }

    public void setLetterRanges(List<String> letterRanges) {
        this.letterRanges = letterRanges;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getProposerPort() {
        return proposerPort;
    }

    public void setProposerPort(int proposerPort) {
        this.proposerPort = proposerPort;
    }

    public String getFileStr() {
        return fileStr;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }

    public Set<String> getEligibleLetters() {
        return eligibleLetters;
    }

    public void setEligibleLetters(Set<String> eligibleLetters) {
        this.eligibleLetters = eligibleLetters;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }
}
