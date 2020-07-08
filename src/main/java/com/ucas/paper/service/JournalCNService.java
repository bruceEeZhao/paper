package com.ucas.paper.service;

import com.ucas.paper.entities.JournalCN;
import com.ucas.paper.entities.JournalCNSearch;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JournalCNService {
    JournalCN addJournal(JournalCN journal);

    JournalCN getJournal(Long id);

    JournalCN getJournalByName(String name);

    JournalCN getJournalByIssn(String issn);

    Page<JournalCN> listJournal(Pageable pageable);

    Page<JournalCN> listJournal(Pageable pageable, JournalCNSearch search);

    List<JournalCN> listJournal();

    JournalCN updateJournalById(Long id, JournalCN journal);

    void deleteJournalById(Long id);

    void deleteAllJournal();

    public XSSFWorkbook show();

    Long count();
}
