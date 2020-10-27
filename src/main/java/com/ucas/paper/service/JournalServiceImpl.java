package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.JournalRespository;
import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.handler.JournalComparator;
import com.ucas.paper.handler.JournalComparatorEng;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class JournalServiceImpl implements JournalService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JournalRespository journalRespository;

    @Transactional
    @Override
    public Journal addJournal(Journal journal) {
        if (journalRespository.findByIssn(journal.getIssn()) != null) {
            //不能添加重复issn的数据
            return null;
        }
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
    public List<Journal> listJournal(String lang) {
        List<Journal> journals = journalRespository.findAll();
        if (lang.equals("zh_cn")) {
            Collections.sort(journals, new JournalComparator());
        } else {
            Collections.sort(journals, new JournalComparatorEng());
        }
        return journals;
    }

    @Transactional
    @Override
    public Page<Journal> listJournal(Pageable pageable, String lang) {
//        Page<Journal> journals = journalRespository.findAllByQuery(pageable);
//        List<Journal> journalList = new ArrayList<>(journals.getContent());
        List<Journal> journalList= journalRespository.findAll();
        //JournalComparator e = new JournalComparator();
        if (lang.equals("zh_cn")) {
            journalList.sort(new JournalComparator());
        } else {
            journalList.sort(new JournalComparatorEng());
        }

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > journalList.size() ? journalList.size() : ( start + pageable.getPageSize());


        return new PageImpl<Journal>(journalList.subList(start, end), pageable, journalList.size());
    }

    @Override
    public Page<Journal> listJournal(Pageable pageable, JournalSearch search, String lang) {
        List<Journal> journalList = journalRespository.findAll(new Specification<Journal>() {
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
        });

        //JournalComparator e = new JournalComparator();
        if (lang.equals("zh_cn")) {
            journalList.sort(new JournalComparator());
        } else {
            journalList.sort(new JournalComparatorEng());
        }

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > journalList.size() ? journalList.size() : ( start + pageable.getPageSize());

        return new PageImpl<Journal>(journalList.subList(start, end), pageable, journalList.size());
    }

    @Transactional
    @Override
    public Journal updateJournalById(Long id, Journal journal) {
        Journal j = journalRespository.findById(id).orElse(null);

        if (j == null) {
            throw new NotFoundException("不存在该类型");
        }
        Date date = j.getCreateTime();
        BeanUtils.copyProperties(journal, j);
        j.setUpdateTime(new Date());
        j.setCreateTime(date);
        return journalRespository.save(j);
    }

    @Transactional
    @Override
    public void deleteJournalById(Long id) {
        journalRespository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllJournal() {
        journalRespository.deleteAll();
    }

    /**
     *     private Long id;
     *     private String issn;
     *     private Type subject;
     *     private String name;  //期刊名称
     *     private String fms;
     *     private Integer jcr;
     *     private Integer sjr;
     *     private Integer snip;
     *     private Integer ipp;
     * */

    @Override
    public XSSFWorkbook show() {
        List<Journal> list = journalRespository.findAll();
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("journal");//创建一张表
        Row titleRow = sheet.createRow(0);//创建第一行，起始为0
        titleRow.createCell(0).setCellValue("序号");//第一列
        titleRow.createCell(1).setCellValue("issn");
        titleRow.createCell(2).setCellValue("学科");
        titleRow.createCell(3).setCellValue("英文学科");
        titleRow.createCell(4).setCellValue("期刊名称");
        titleRow.createCell(5).setCellValue("fms");
        titleRow.createCell(6).setCellValue("jcr");
        titleRow.createCell(7).setCellValue("sjr");
        titleRow.createCell(8).setCellValue("snip");
        int cell = 1;
        for (Journal journal : list) {
            Row row = sheet.createRow(cell);//从第二行开始保存数据
            row.createCell(0).setCellValue(cell);
            row.createCell(1).setCellValue(journal.getIssn());//将数据库的数据遍历出来
            row.createCell(2).setCellValue(journal.getSubject().getName());
            row.createCell(3).setCellValue(journal.getSubject().getEngName());
            row.createCell(4).setCellValue(journal.getName());
            row.createCell(5).setCellValue(journal.getFms());
            row.createCell(6).setCellValue(journal.getJcr());
            row.createCell(7).setCellValue(journal.getSjr());
            row.createCell(8).setCellValue(journal.getSnip());

            cell++;
        }
        return wb;
    }


    @Override
    public Long count() {
        return journalRespository.count();
    }


}

