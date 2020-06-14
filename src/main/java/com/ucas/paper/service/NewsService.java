package com.ucas.paper.service;

import com.ucas.paper.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    News addNews(News news);
    News getNews(Long id);
    News getAndConvert(Long id);
    Page<News> getNewsByTitle(String title, Pageable pageable);

    //游客搜索
    Page<News> getNewsByTitlePublished(String title, Pageable pageable);
    News updateNewsById(Long id, News news);
    Page<News> listNews(Pageable pageable);
    Page<News> listPublishedNews(Pageable pageable);
    List <News> listPublishedNesw(Integer size, Integer pagenum);
    List <News> listPublishedNeswTop5();

    void delNews(Long id);
}
