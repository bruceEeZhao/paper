package com.ucas.paper.service;

import com.ucas.paper.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService {
    News addNews(News news);
    News getNews(Long id);
    Page<News> getNewsByTitle(String title, Pageable pageable);
    News updateNewsById(Long id, News news);
    Page<News> listNews(Pageable pageable);
    void delNews(Long id);
}
