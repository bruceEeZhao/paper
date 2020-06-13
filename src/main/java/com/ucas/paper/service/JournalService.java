package com.ucas.paper.service;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.JournalSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JournalService {

    Journal addJournal(Journal journal);

    Journal getJournal(Long id);

    Journal getJournalByName(String name);

    Journal getJournalByIssn(String issn);

    Page<Journal> listJournal(Pageable pageable);

    Page<Journal> listJournal(Pageable pageable, JournalSearch search);

    List<Journal> listJournal();

    Journal updateJournalById(Long id, Journal journal);

    void deleteJournalById(Long id);

}
