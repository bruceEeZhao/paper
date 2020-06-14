package com.ucas.paper.controller;

import com.ucas.paper.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class NewsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String newslist(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        model.addAttribute("page", newsService.listPublishedNews(pageable));
        return "news/newslist";
    }

    @GetMapping("/news/{id}")
    public String newsread(@PathVariable("id") Long id,
                           Model model) {
        model.addAttribute("news", newsService.getAndConvert(id));
        return "news/newsread";
    }
}
