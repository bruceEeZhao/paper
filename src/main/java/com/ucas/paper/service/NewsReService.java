package com.ucas.paper.service;

import com.ucas.paper.entities.NewsRecommend;

public interface NewsReService {
    NewsRecommend getNewRe();
    NewsRecommend updateRe(NewsRecommend nr);
}
