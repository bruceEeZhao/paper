package com.ucas.paper.controller;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    //返回journal列表
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Journal journal, Model model) {
        model.addAttribute("page",journalService.listJournal(pageable));
        return "journal/list";
    }
}
