package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.SpecialistResponsitory;
import com.ucas.paper.entities.Specialist;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Specialist> listSpecialist(Pageable pageable) {
        return specialistResponsitory.findAll(pageable);
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
}
