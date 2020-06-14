package com.ucas.paper.dao;

import com.ucas.paper.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRespository extends JpaRepository<News, Long> {
    News findByTitle(String title);

    @Query("select b from News b where b.title like ?1")
    Page<News> findByQuery(String query, Pageable pageable);

    @Query("select b from News b where b.title like ?1 and b.published=true")
    Page<News> findByQueryPublished(String query, Pageable pageable);

    @Query("select b from News b where b.published=true")
    List<News> findTop(Pageable pageable);

    @Query("select b from News b where b.published=true")
    Page<News> findTopp(Pageable pageable);
}
