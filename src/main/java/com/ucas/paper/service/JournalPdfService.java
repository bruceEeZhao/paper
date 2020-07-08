package com.ucas.paper.service;

import com.ucas.paper.entities.JournalPdf;

public interface JournalPdfService {
    JournalPdf getByName(String name);
    JournalPdf addOne(JournalPdf journalPdf);
    JournalPdf upDate(JournalPdf journalPdf);
}
