package com.ucas.paper.dao;

import com.ucas.paper.entities.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpecialistResponsitory extends JpaRepository<Specialist, Long> {
}
