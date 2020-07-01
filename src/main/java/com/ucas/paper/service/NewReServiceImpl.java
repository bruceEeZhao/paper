package com.ucas.paper.service;

import com.ucas.paper.dao.NewsReRespository;
import com.ucas.paper.entities.NewsRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewReServiceImpl implements NewsReService {

    @Autowired
    private NewsReRespository newsReRespository;

    @Override
    public NewsRecommend getNewRe() {
        List<NewsRecommend> nr = newsReRespository.findAll();
        if (nr.isEmpty()) {
            return null;
        }
        else {
            return nr.get(0);
        }
    }

    @Transactional
    @Override
    public NewsRecommend updateRe(NewsRecommend nr) {
        newsReRespository.deleteAll();
        return newsReRespository.save(nr);
    }
}
