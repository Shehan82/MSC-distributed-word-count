package com.dc.coordinator.dto;

import java.util.ArrayList;
import java.util.List;

public class LetterRangeAssignmentDTO {

    private List<String> letterRanges;

    public List<String> getLetterRanges() {
        if (letterRanges == null)
            letterRanges = new ArrayList<>();
        return letterRanges;
    }

    public void setLetterRanges(List<String> letterRanges) {
        this.letterRanges = letterRanges;
    }
}
