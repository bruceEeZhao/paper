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

    @GetMapping("/admin/news/search")
    public String newsSearch(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                         Pageable pageable, Model model, @RequestParam String search_news_a) {

        model.addAttribute("page", newsService.getNewsByTitle("%"+search_news_a+"%", pageable));
        model.addAttribute("search", search_news_a);
        return "admin/news";
    }

    @GetMapping("/admin/journals/search")
    public String journaSearch(Pageable pageable, Model model,
                               Integer page,
                               @RequestParam("subject.id") Long subid,
                               @RequestParam("issn") String issn,
                               @RequestParam("name") String name,
                               @RequestParam(value = "numb_show", defaultValue = "20") Integer numb_show) {
        try {
            if (page==null || page<0) {
                page = 0;
            }

            JournalSearch search = new JournalSearch(subid, issn.trim(), name.trim());
            Sort order = Sort.by(Sort.Direction.DESC, "fms");
            pageable = PageRequest.of(page, numb_show, order);

            model.addAttribute("subjects", typeService.listType());
            model.addAttribute("page",journalService.listJournal(pageable, search));
            model.addAttribute("numb_show", numb_show);
            model.addAttribute("subid",subid);
            model.addAttribute("issn", issn);
            model.addAttribute("name", name);

            model.addAttribute("search",1);

        } catch (Exception e) {
            logger.error(e.toString());
        } finally {

        }

        return "admin/journalList";
    }


    @GetMapping("/admin/types/search")
    public String typesSearch(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model,
                              @RequestParam("type") String type) {
        model.addAttribute("page",typeService.listType(pageable, type));
        model.addAttribute("search",type);
        return "admin/types";
    }
}
