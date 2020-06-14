package com.ucas.paper.controller;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.Type;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class JournalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Journal 部分
     * */

    @Autowired
    private JournalService journalService;

    @Autowired
    private TypeService typeService;

    //返回journal列表
    @GetMapping("/journals")
    public String journaLlist(@PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model) {
        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("page",journalService.listJournal(pageable));
        return "journal/list";
    }

}
