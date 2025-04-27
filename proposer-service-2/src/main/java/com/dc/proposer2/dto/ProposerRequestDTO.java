package com.dc.proposer2.dto;

public class ProposerRequestDTO {

    private String fileStr;

    private String letterRangeStr;

    private String lineID;

    public String getFileStr() {
        return fileStr;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }

    public String getLetterRangeStr() {
        return letterRangeStr;
    }

    public void setLetterRangeStr(String letterRangeStr) {
        this.letterRangeStr = letterRangeStr;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    @Override
    public String toString() {
        return "ProposerRequestDTO{" +
                "fileStr='" + fileStr + '\'' +
                ", letterRangeStr='" + letterRangeStr + '\'' +
                '}';
    }
}
