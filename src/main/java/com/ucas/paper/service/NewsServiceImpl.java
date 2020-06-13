package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.NewsRespository;
import com.ucas.paper.entities.News;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRespository newsRespository;

    @Transactional
    @Override
    public News addNews(News news) {
        return newsRespository.save(news);
    }

    @Transactional
    @Override
    public News getNews(Long id) {
        return newsRespository.getOne(id);
    }

    @Override
    public Page<News> getNewsByTitle(String title, Pageable pageable) {
        return newsRespository.findByQuery(title, pageable);
    }

    @Transactional
    @Override
    public News updateNewsById(Long id, News news) {
        News n = newsRespository.findById(id).orElse(null);
        if (n==null) {
            throw new NotFoundException("不存在该记录");
        }
        BeanUtils.copyProperties(news, n);
        return newsRespository.save(n);
    }

    @Transactional
    @Override
    public Page<News> listNews(Pageable pageable) {
        return newsRespository.findAll(pageable);
    }

    @Transactional
    @Override
    public void delNews(Long id) {
        newsRespository.deleteById(id);
    }
}
