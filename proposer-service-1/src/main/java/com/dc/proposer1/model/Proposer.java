package com.dc.proposer1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_proposer")
public class Proposer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROPOSER_ID")
    private Integer id;

    @Column(name = "PORT")
    private Integer port;

    @Column(name = "LETTER_RANGE")
    private String letterRange;

    @Column(name = "STATUS")
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLetterRange() {
        return letterRange;
    }

    public void setLetterRange(String letterRange) {
        this.letterRange = letterRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
