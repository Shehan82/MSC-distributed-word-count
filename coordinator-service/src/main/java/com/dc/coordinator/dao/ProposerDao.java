package com.dc.coordinator.dao;

import com.dc.coordinator.model.Proposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProposerDao extends JpaRepository<Proposer, Integer> {
    List<Proposer> findByPortAndStatus(int port, String status);

    List<Proposer> findByLetterRangeInAndStatus(List<String> letterRange, String status);
}
