package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.JournalRespository;
import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.entities.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JournalServiceImpl implements JournalService {

    @Autowired
    private JournalRespository journalRespository;

    @Transactional
    @Override
    public Journal addJournal(Journal journal) {
        journal.setCreateTime(new Date());
        journal.setUpdateTime(new Date());
        return journalRespository.save(journal);
    }

    @Transactional
    @Override
    public Journal getJournal(Long id) {
        return journalRespository.getOne(id);
    }

    @Transactional
    @Override
    public Journal getJournalByName(String name) {
        return journalRespository.findByName(name);
    }

    @Transactional
    @Override
    public Journal getJournalByIssn(String issn) {
        return journalRespository.findByIssn(issn);
    }

    @Override
    public List<Journal> listJournal() {
        return journalRespository.findAll();
    }

    @Transactional
    @Override
    public Page<Journal> listJournal(Pageable pageable) {
        return journalRespository.findAll(pageable);
    }

    @Override
    public Page<Journal> listJournal(Pageable pageable, JournalSearch search) {
        return journalRespository.findAll(new Specification<Journal>() {
            @Override
            public Predicate toPredicate(Root<Journal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (search.getIssn() != null && !"".equals(search.getIssn())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("issn"),"%" +search.getIssn()+"%"));
                }
                if (search.getName()!=null && !"".equals(search.getName())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("name"),"%" +search.getName()+"%"));
                }
                if (search.getSubjectId()!=null) {
                    predicates.add(criteriaBuilder.equal(root.<Long>get("subject").get("id"),search.getSubjectId()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Journal updateJournalById(Long id, Journal journal) {
        Journal j = journalRespository.findById(id).orElse(null);

        if (j == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(journal, j);
        j.setUpdateTime(new Date());
        return journalRespository.save(j);
    }

    @Transactional
    @Override
    public void deleteJournalById(Long id) {
        journalRespository.deleteById(id);
    }

}
