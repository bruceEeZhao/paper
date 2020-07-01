package com.ucas.paper.dao;

import com.ucas.paper.entities.NewsRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsReRespository extends JpaRepository<NewsRecommend, Long> {
}
