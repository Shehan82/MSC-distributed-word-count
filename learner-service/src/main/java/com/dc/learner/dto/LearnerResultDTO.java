package com.dc.learner.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class LearnerResultDTO {

    private String letter;

    private Integer wordCount;

    private TreeSet<String> words;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public TreeSet<String> getWords() {
        if(this.words == null)
            this.words = new TreeSet<>();
        return words;
    }

    public void setWords(TreeSet<String> words) {
        this.words = words;
    }
}
