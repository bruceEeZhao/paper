package com.ucas.paper.dao;

import com.ucas.paper.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRespository extends JpaRepository<News, Long> {
    News findByTitle(String title);

    @Query("select b from News b where b.title like ?1")
    Page<News> findByQuery(String query, Pageable pageable);
}
