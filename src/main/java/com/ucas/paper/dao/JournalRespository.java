package com.ucas.paper.dao;

import com.ucas.paper.entities.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface JournalRespository extends JpaRepository<Journal, Long>, JpaSpecificationExecutor<Journal> {
    Journal findByName(String name);
    Journal findByIssn(String issn);
}
