package com.dc.proposer2.dao;

import com.dc.proposer2.model.Proposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposerDao extends JpaRepository<Proposer, Integer> {
    List<Proposer> findByPortAndStatus(int port, String status);
}
