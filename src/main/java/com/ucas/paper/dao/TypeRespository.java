package com.ucas.paper.dao;

import com.ucas.paper.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TypeRespository extends JpaRepository<Type, Long> {
    Type findByName(String name);

    @Query("select b from Type b where b.name like ?1")
    Page<Type> findByQuery(String query, Pageable pageable);
}
