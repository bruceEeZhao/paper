package com.ucas.paper.controller;

import com.ucas.paper.entities.JournalCNSearch;
import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private JournalCNService journalCNService;

    @Autowired
    private NewsReService newsReService;

    // 普通用户的搜索

    @GetMapping("/news/search")
    public String newsSearch_n(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam String search_news_a,  Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        model.addAttribute("page", newsService.getNewsByTitlePublished("%"+search_news_a+"%", pageable));
        model.addAttribute("search",search_news_a);
        model.addAttribute("newr", newsReService.getNewRe());
        return "news/newslist";
    }

    @GetMapping("/journals/search")
    public String journaSearch_n(Pageable pageable, Model model,
                                 Integer page,
                                 @RequestParam("subject.id") Long subid,
                                 @RequestParam("issn") String issn,
                                 @RequestParam("name") String name,
                                 @RequestParam(value = "num", defaultValue = "20") Integer numb_show) {
        try {
            if (page==null || page<0) {
                page = 0;
            }

            if (numb_show>0) {
                JournalSearch search = new JournalSearch(subid, issn.trim(), name.trim());
                pageable = PageRequest.of(page, numb_show);
                model.addAttribute("page",journalService.listJournal(pageable, search));
            } else {
                model.addAttribute("pagel",journalService.listJournal());
            }

            model.addAttribute("subjects", typeService.listType());
            model.addAttribute("numb_show", numb_show);
            model.addAttribute("subid",subid);
            model.addAttribute("issn", issn);
            model.addAttribute("name", name);

            model.addAttribute("search",1);

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {

        }

        return "journal/list";
    }

    @GetMapping("/journals_cn/search")
    public String journaSearch_cn(Pageable pageable, Model model,
                                 Integer page,
                                 @RequestParam("issn") String issn,
                                 @RequestParam("name") String name,
                                 @RequestParam(value = "num", defaultValue = "20") Integer numb_show) {
        try {
            if (page==null || page<0) {
                page = 0;
            }

            if (numb_show>0) {
                JournalCNSearch search = new JournalCNSearch(issn.trim(), name.trim());
                pageable = PageRequest.of(page, numb_show);
                model.addAttribute("page",journalCNService.listJournal(pageable, search));
            } else {
                model.addAttribute("pagel",journalCNService.listJournal());
            }

            model.addAttribute("numb_show", numb_show);
            model.addAttribute("issn", issn);
            model.addAttribute("name", name);

            model.addAttribute("search",1);

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {

        }

        return "journal/list_cn";
    }


    @GetMapping("/index_search")
    public String indexSearch(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam("indexSearch") String search_news_a, Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        model.addAttribute("page", newsService.getNewsByTitlePublished("%"+search_news_a+"%", pageable));
        model.addAttribute("search",search_news_a);
        model.addAttribute("newr", newsReService.getNewRe());
        return "news/newslist";
    }
}
