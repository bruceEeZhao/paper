package com.ucas.paper.service;

import com.ucas.paper.entities.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpecialistService {

    Specialist addSpecialist(Specialist specialist);
    Specialist getSpecialist(Long id);
    Specialist getAndConvert(Long id);
    Specialist updateSpecialist(Long id, Specialist specialist);
    Page<Specialist> listSpecialist(Pageable pageable);
    List<Specialist> listSpecialist();
    void delSpecialist(Long id);
}
