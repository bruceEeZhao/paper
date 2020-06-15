package com.ucas.paper.dao;

import com.ucas.paper.entities.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SpecialistResponsitory extends JpaRepository<Specialist, Long> {
}
