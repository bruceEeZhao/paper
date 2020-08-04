package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.JournalCNRespository;
import com.ucas.paper.entities.JournalCN;
import com.ucas.paper.entities.JournalCNSearch;
import com.ucas.paper.handler.JournalCNComparator;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class JournalCNServiceImpl implements JournalCNService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JournalCNRespository journalRespository;

    @Transactional
    @Override
    public JournalCN addJournal(JournalCN journal) {
        if (journalRespository.findByIssn(journal.getIssn()) != null) {
            //不能添加重复issn的数据
            return null;
        }
        journal.setCreateTime(new Date());
        journal.setUpdateTime(new Date());
        return journalRespository.save(journal);
    }


    @Override
    public JournalCN getJournal(Long id) {
        return journalRespository.getOne(id);
    }

    @Override
    public JournalCN getJournalByName(String name) {
        return journalRespository.findByName(name);
    }

    @Override
    public JournalCN getJournalByIssn(String issn) {
        return journalRespository.findByIssn(issn);
    }

    @Override
    public Page<JournalCN> listJournal(Pageable pageable) {
        List<JournalCN> journalList= journalRespository.findAll();
        JournalCNComparator e = new JournalCNComparator();
        journalList.sort(e);

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > journalList.size() ? journalList.size() : ( start + pageable.getPageSize());


        return new PageImpl<JournalCN>(journalList.subList(start, end), pageable, journalList.size());
    }

    @Override
    public Page<JournalCN> listJournal(Pageable pageable, JournalCNSearch search) {
        List<JournalCN> journalList = journalRespository.findAll(new Specification<JournalCN>() {
            @Override
            public Predicate toPredicate(Root<JournalCN> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (search.getIssn() != null && !"".equals(search.getIssn())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("issn"),"%" +search.getIssn()+"%"));
                }
                if (search.getName()!=null && !"".equals(search.getName())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("name"),"%" +search.getName()+"%"));
                }

                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });

        JournalCNComparator e = new JournalCNComparator();
        journalList.sort(e);

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > journalList.size() ? journalList.size() : ( start + pageable.getPageSize());

        return new PageImpl<JournalCN>(journalList.subList(start, end), pageable, journalList.size());
    }

    @Override
    public List<JournalCN> listJournal() {
        List<JournalCN> journals = journalRespository.findAll();
        Collections.sort(journals, new JournalCNComparator());
        return journals;
    }

    @Transactional
    @Override
    public JournalCN updateJournalById(Long id, JournalCN journal) {
        JournalCN j = journalRespository.findById(id).orElse(null);

        if (j == null) {
            throw new NotFoundException("不存在该类型");
        }
        Date date = j.getCreateTime();
        BeanUtils.copyProperties(journal, j);
        j.setId(id);
        j.setUpdateTime(new Date());
        j.setCreateTime(date);
        return journalRespository.save(j);
    }

    @Transactional
    @Override
    public void deleteJournalById(Long id) {
        journalRespository.deleteById(id);
    }

    @Override
    public void deleteAllJournal() {
        journalRespository.deleteAll();
    }

    @Override
    public XSSFWorkbook show() {
        List<JournalCN> list = journalRespository.findAll();
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("journal");//创建一张表
        Row titleRow = sheet.createRow(0);//创建第一行，起始为0
        titleRow.createCell(0).setCellValue("序号");//第一列
        titleRow.createCell(1).setCellValue("issn");
        titleRow.createCell(2).setCellValue("期刊名称");
        titleRow.createCell(3).setCellValue("fms");
        titleRow.createCell(4).setCellValue("主办单位");
        int cell = 1;
        for (JournalCN journal : list) {
            Row row = sheet.createRow(cell);//从第二行开始保存数据
            row.createCell(0).setCellValue(cell);
            row.createCell(1).setCellValue(journal.getIssn());//将数据库的数据遍历出来
            row.createCell(2).setCellValue(journal.getName());
            row.createCell(3).setCellValue(journal.getFms());
            row.createCell(4).setCellValue(journal.getHost());
            cell++;
        }
        return wb;
    }

    @Override
    public Long count() {
        return journalRespository.count();
    }
}
