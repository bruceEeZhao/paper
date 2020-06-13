package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.JournalSearch;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminJournalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Journal 部分
     * */

    @Autowired
    private JournalService journalService;

    @Autowired
    private TypeService typeService;

    //返回journal列表
    @GetMapping("journals")
    public String journaLlist(@PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable, Model model) {
        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("page",journalService.listJournal(pageable));
        return "admin/journalList";
    }

    @GetMapping("journal/input")
    public String journalInput(Model model,RedirectAttributes attributes) {
        List<Type> tmp = typeService.listType();
        if(tmp == null || tmp.isEmpty()) {
            attributes.addFlashAttribute("message", "请先添加学科");
            return "redirect:/admin/journals";
        }
        model.addAttribute("subjects", typeService.listType());
        return "admin/journalInput";
    }

    @GetMapping("journal/{id}/input")
    public String journalEditInput(@PathVariable("id")  Long id,
                                   Model model,
                                   RedirectAttributes attributes) {
        List<Type> tmp = typeService.listType();
        if(tmp == null || tmp.isEmpty()) {
            attributes.addFlashAttribute("message", "请先添加学科");
            return "redirect:/admin/journals";
        }
        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("journal",journalService.getJournal(id));
        return "admin/journalInput";
    }

    @PostMapping("journal/edit{id}")
    public String journalEdit(@Valid Journal journal,
                              BindingResult result,
                              @PathVariable("id") Long id,
                              RedirectAttributes attributes) {

        Journal journal1 = journalService.getJournalByIssn(journal.getIssn());

        if (id == null) {
            //新增
            if (journal1 != null) {
                result.rejectValue("issn", "nameError", "不允许重复的issn");
            }

            List<Type> tmp = typeService.listType();
            if(tmp == null || tmp.isEmpty()) {
                result.rejectValue("type", "nameError", "请先添加学科");
            }
        } else {
            //修改
            if (journal1 == null) {
                result.rejectValue("issn", "nameError", "数据库中不存在该记录");
            }
        }

        if (result.hasErrors()) {
            logger.info("传递参数有误:" + result.getFieldError().toString());

            String errorms = "";
            List<FieldError> fieldErrors = result.getFieldErrors();
            for(int i=0;i<fieldErrors.size();i++) {
                errorms += fieldErrors.get(i).getDefaultMessage() + "\n";
            }

            attributes.addFlashAttribute("message",errorms);

            if(id == null) {
                return "redirect:/admin/journal/input";
            } else {
                String s = "redirect:/admin/journal/" + id +"/input";
                return s;
            }
        }

        journal.setSubject(typeService.getType(journal.getSubject().getId()));

        Journal j = null;
        if (id == null) {
            j = journalService.addJournal(journal);
        } else {
            j = journalService.updateJournalById(id, journal);
        }
        if (j == null) {
            //失败
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/journals";
    }

    @GetMapping("journal/{id}/delete")
    public String journalDelete(@PathVariable("id") Long id,
                                RedirectAttributes attributes) {
        Journal journal1 = journalService.getJournal(id);
        if(journal1 == null) {
            logger.info("不存在该记录，不能删除");
            attributes.addFlashAttribute("message","不存在该记录，不能删除");

            return "redirect:/admin/journals";
        }

        try {
            journalService.deleteJournalById(id);
            attributes.addFlashAttribute("message", "删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            attributes.addFlashAttribute("message",e.toString());

        } finally {
            return "redirect:/admin/journals";
        }

    }

}
