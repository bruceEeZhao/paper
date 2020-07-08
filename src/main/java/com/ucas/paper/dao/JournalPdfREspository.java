package com.ucas.paper.dao;

import com.ucas.paper.entities.JournalPdf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalPdfREspository extends JpaRepository<JournalPdf, Long> {
    JournalPdf findByName(String name);
}
