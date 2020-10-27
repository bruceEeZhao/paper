package com.ucas.paper.controller;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.Type;
import com.ucas.paper.service.JournalCNService;
import com.ucas.paper.service.JournalService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
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
    private JournalCNService journalCNService;

    @Autowired
    private TypeService typeService;

    //返回journal列表
    @GetMapping("/journals")
    public String journaLlist(Pageable pageable, Model model,Integer page,
                              @RequestParam(value = "num", defaultValue = "20") Integer num,
                              @RequestParam(value = "lang", defaultValue = "zh_cn") String lang) {
        if (page == null || page < 0){
            page = 0;
        }

        if (lang == null) {
            lang = "zh_cn";
        }

        Sort order = Sort.by(Sort.Direction.DESC, "fms");
        pageable = PageRequest.of(page,num,order);

        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("page", journalService.listJournal(pageable, lang));
        model.addAttribute("numb_show", num);
        model.addAttribute("lang", lang);
        return "journal/list";
    }

    //返回journal列表
    @GetMapping("/journals_cn")
    public String journaLlist_cn(Pageable pageable, Model model,Integer page,
                              @RequestParam(value = "num", defaultValue = "20") Integer num) {
        if (page == null || page < 0){
            page = 0;
        }

        Sort order = Sort.by(Sort.Direction.DESC, "fms");
        pageable = PageRequest.of(page,num,order);

        model.addAttribute("page",journalCNService.listJournal(pageable));
        model.addAttribute("numb_show", num);
        return "journal/list_cn";
    }
}
