package com.ucas.paper.service;

import com.ucas.paper.dao.JournalPdfREspository;
import com.ucas.paper.entities.JournalPdf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JournalPdfServiceImpl implements JournalPdfService {

    @Autowired
    private JournalPdfREspository journalPdfREspository;

    @Override
    public JournalPdf getByName(String name) {
        return journalPdfREspository.findByName(name);
    }

    @Transactional
    @Override
    public JournalPdf addOne(JournalPdf journalPdf) {
        if (getByName(journalPdf.getName()) != null) {
            return null;
        }
        return journalPdfREspository.save(journalPdf);
    }

    @Transactional
    @Override
    public JournalPdf upDate(JournalPdf journalPdf) {
        JournalPdf j = journalPdfREspository.findByName(journalPdf.getName());
        if (j == null) {
            return journalPdfREspository.save(journalPdf);
        }
        j.setFilename(journalPdf.getFilename());
        j.setAlias(journalPdf.getAlias());

        return journalPdfREspository.save(j);
    }
}
