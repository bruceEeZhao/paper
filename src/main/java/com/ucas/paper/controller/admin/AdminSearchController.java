package com.ucas.paper.controller.admin;

import com.ucas.paper.dao.JournalRespository;
import com.ucas.paper.dao.NewsRespository;
import com.ucas.paper.dao.SpecialistResponsitory;
import com.ucas.paper.entities.JournalSearch;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.NewsService;
import com.ucas.paper.service.SpecialistService;
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
public class AdminSearchController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewsService newsService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private TypeService typeService;

    @PostMapping("/admin/news/search")
    public String newsSearch(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                         @RequestParam String search_news_a, Pageable pageable, Model model) {

        model.addAttribute("page", newsService.getNewsByTitle("%"+search_news_a+"%", pageable));
        return "admin/news";
    }

    @PostMapping("/admin/journals/search")
    public String journaSearch(@PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC)
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

        return "admin/journalList";
    }


    @PostMapping("/admin/types/search")
    public String typesSearch(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model,
                              @RequestParam("type") String type) {
        model.addAttribute("page",typeService.listType(pageable, type));
        return "admin/types";
    }
}
