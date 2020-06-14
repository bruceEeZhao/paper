package com.ucas.paper.controller;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.NewsService;
import com.ucas.paper.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class IndexController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private JournalService journalService;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Pageable pageable, Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));

        return "index";
    }

    @GetMapping("/aboutus")
    public String aboutus() {
        return "aboutus";
    }
}
