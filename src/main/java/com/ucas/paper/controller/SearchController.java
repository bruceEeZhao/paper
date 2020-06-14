package com.ucas.paper.controller;

import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.NewsService;
import com.ucas.paper.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private NewsService newsService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private JournalService journalService;

    // 普通用户的搜索

    @PostMapping("/news/search")
    public String newsSearch_n(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                               @RequestParam String search_news_a, Pageable pageable, Model model) {

        model.addAttribute("page", newsService.getNewsByTitlePublished("%"+search_news_a+"%", pageable));
        return "news/newslist";
    }

    @PostMapping("/journals/search")
    public String journaSearch_n(@PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, Model model,
                                 @RequestParam("subject.id") Long subid,
                                 @RequestParam("issn") String issn,
                                 @RequestParam("name") String name,
                                 @RequestParam("numb_show") Long numb_show) {
        try {
            JournalSearch search = new JournalSearch(subid, issn.trim(), name.trim());


            model.addAttribute("subjects", typeService.listType());
            model.addAttribute("page",journalService.listJournal(pageable, search));

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {

        }

        return "journal/list";
    }
}
