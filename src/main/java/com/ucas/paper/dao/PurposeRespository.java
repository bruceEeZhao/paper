package com.ucas.paper.dao;

import com.ucas.paper.entities.Purpose;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface PurposeRespository extends JpaRepository<Purpose, Long> {
    @Query("select b from Purpose b order by b.id desc")
    List<Purpose> findTop(Pageable pageable);
}
