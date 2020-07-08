package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.NewsRespository;
import com.ucas.paper.entities.News;
import com.ucas.paper.handler.MarkdownHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRespository newsRespository;

    @Transactional
    @Override
    public News addNews(News news) {
        news.setCreateTime(new Date());
        news.setUpdateTime(new Date());

        Integer len = 0;
        if (news.getContent().length() > 128) {
            len = 128;
        } else {
            len = news.getContent().length() - 1;
        }
        news.setDescription(news.getContent().substring(0,len));
        return newsRespository.save(news);
    }

    @Transactional
    @Override
    public News getNews(Long id) {

        return newsRespository.getOne(id);
    }

    @Override
    public News getAndConvert(Long id) {
        News news = newsRespository.findById(id).orElse(null);
        if (news == null) {
            throw new NotFoundException("新闻不存在");
        }
        News n = new News();
        BeanUtils.copyProperties(news, n);
        String content = n.getContent();

        n.setContent(MarkdownHandler.markdownToHtml(content));
        return n;
    }

    @Override
    public Page<News> getNewsByTitle(String title, Pageable pageable) {
        return newsRespository.findByQuery(title, pageable);
    }

    @Override
    public Page<News> getNewsByTitlePublished(String title, Pageable pageable) {
        return newsRespository.findByQueryPublished(title, pageable);
    }

    @Transactional
    @Override
    public News updateNewsById(Long id, News news) {
        News n = newsRespository.findById(id).orElse(null);
        if (n==null) {
            throw new NotFoundException("不存在该记录");
        }
        Date date=n.getCreateTime();
        BeanUtils.copyProperties(news, n);
        n.setUpdateTime(new Date());
        n.setCreateTime(date);

        Integer len = 0;
        if (n.getContent().length() > 128) {
            len = 128;
        } else {
            len = n.getContent().length() - 1;
        }
        n.setDescription(n.getContent().substring(0,len));
        return newsRespository.save(n);
    }

    @Transactional
    @Override
    public Page<News> listNews(Pageable pageable) {
        return newsRespository.findAll(pageable);
    }

    @Override
    public Page<News> listPublishedNews(Pageable pageable) {
        return newsRespository.findTopp(pageable);
    }

    @Transactional
    @Override
    public List<News> listNews() {
        return newsRespository.findAll();
    }

    @Override
    public List<News> listPublishedNesw(Integer size, Integer pagenum) {
        Sort order = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(pagenum,size,order);
        return newsRespository.findTop(pageable);
    }

    @Transactional
    @Override
    public void delNews(Long id) {
        newsRespository.deleteById(id);
    }

    @Override
    public Long count() {
        return newsRespository.count();
    }
}
