package com.ucas.paper.dao;

import com.ucas.paper.entities.JournalCN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalCNRespository extends JpaRepository<JournalCN, Long>, JpaSpecificationExecutor<JournalCN> {
    JournalCN findByName(String name);
    JournalCN findByIssn(String issn);
}
