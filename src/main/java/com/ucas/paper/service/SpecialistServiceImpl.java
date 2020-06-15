package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.SpecialistResponsitory;
import com.ucas.paper.entities.Specialist;
import com.ucas.paper.handler.MarkdownHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialistServiceImpl implements SpecialistService {

    @Autowired
    private SpecialistResponsitory specialistResponsitory;

    @Transactional
    @Override
    public Specialist addSpecialist(Specialist specialist) {
        return specialistResponsitory.save(specialist);
    }

    @Transactional
    @Override
    public Specialist getSpecialist(Long id) {
        return specialistResponsitory.getOne(id);
    }

    @Transactional
    @Override
    public Specialist getAndConvert(Long id) {
        Specialist specialist = specialistResponsitory.findById(id).orElse(null);
        if (specialist == null) {
            throw new NotFoundException("数据库中不存在该记录");
        }

        Specialist s = new Specialist();
        BeanUtils.copyProperties(specialist, s);
        String content = s.getBrief();
        s.setBrief(MarkdownHandler.markdownToHtml(content));
        return s;
    }

    @Transactional
    @Override
    public Page<Specialist> listSpecialist(Pageable pageable) {
        return specialistResponsitory.findAll(pageable);
    }


    @Override
    public List<Specialist> listSpecialist() {
        List<Specialist> findall = specialistResponsitory.findAll();

        if (findall.size()==0 || findall==null) {
            return findall;
        }

        List<Specialist> specialists = new ArrayList<>();

        for (Specialist item : findall) {
            Specialist tmp = new Specialist();
            BeanUtils.copyProperties(item, tmp);
            String content = tmp.getBrief();
            tmp.setBrief(MarkdownHandler.markdownToHtml(content));
            specialists.add(tmp);
        }
        return specialists;
    }

    @Override
    public Specialist updateSpecialist(Long id, Specialist specialist) {
        Specialist s = specialistResponsitory.findById(id).orElse(null);
        if (s == null) {
            throw new NotFoundException("没有该记录");
        }

        BeanUtils.copyProperties(specialist, s);
        return specialistResponsitory.save(s);
    }

    @Transactional
    @Override
    public void delSpecialist(Long id) {
        specialistResponsitory.deleteById(id);
    }

    @Override
    public Long count() {
        return specialistResponsitory.count();
    }
}
