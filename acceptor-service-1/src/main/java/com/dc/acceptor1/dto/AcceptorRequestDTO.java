package com.dc.acceptor1.dto;

import java.util.List;

public class AcceptorRequestDTO {

    private String letterRangeStr;

    private int wordCount;

    private int proposerPort;

    private String fileStr;

    private List<String> eligibleLetters;

    public String getLetterRangeStr() {
        return letterRangeStr;
    }

    public void setLetterRangeStr(String letterRangeStr) {
        this.letterRangeStr = letterRangeStr;
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

    public List<String> getEligibleLetters() {
        return eligibleLetters;
    }

    public void setEligibleLetters(List<String> eligibleLetters) {
        this.eligibleLetters = eligibleLetters;
    }
}
