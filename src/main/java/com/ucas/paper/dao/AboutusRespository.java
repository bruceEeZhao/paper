package com.ucas.paper.dao;

import com.ucas.paper.entities.Aboutus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface AboutusRespository extends JpaRepository<Aboutus, Long> {

    @Query("select b from Aboutus b order by b.id desc")
    List<Aboutus> findTop(Pageable pageable);
}
