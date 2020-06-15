package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.JournalRespository;
import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.entities.Type;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        titleRow.createCell(3).setCellValue("期刊名称");
        titleRow.createCell(4).setCellValue("fms");
        titleRow.createCell(5).setCellValue("jcr");
        titleRow.createCell(6).setCellValue("sjr");
        titleRow.createCell(7).setCellValue("snip");
        titleRow.createCell(8).setCellValue("ipp");
        int cell = 1;
        for (Journal journal : list) {
            Row row = sheet.createRow(cell);//从第二行开始保存数据
            row.createCell(0).setCellValue(cell);
            row.createCell(1).setCellValue(journal.getIssn());//将数据库的数据遍历出来
            row.createCell(2).setCellValue(journal.getSubject().getName());
            row.createCell(3).setCellValue(journal.getName());
            row.createCell(4).setCellValue(journal.getFms());
            row.createCell(5).setCellValue(journal.getJcr());
            row.createCell(6).setCellValue(journal.getSjr());
            row.createCell(7).setCellValue(journal.getSnip());
            row.createCell(8).setCellValue(journal.getIpp());

            cell++;
        }
        return wb;
    }


    @Override
    public Long count() {
        return journalRespository.count();
    }
}
