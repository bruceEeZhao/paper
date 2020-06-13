package com.ucas.paper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
public class NewsController {
    @GetMapping("/list")
    public String newslist() {
        return "news/newslist";
    }

    @GetMapping("/details")
    public String newsread() {
        return "news/newsread";
    }
}
