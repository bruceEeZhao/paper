package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.Specialist;
import com.ucas.paper.service.SpecialistService;
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
@RequestMapping("/admin")
public class AdminSpecialistController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpecialistService specialistService;

    @GetMapping("specialists")
    public String specialistList(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                             Pageable pageable, Model model) {
        try {
            model.addAttribute("page",specialistService.listSpecialist(pageable));
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return "admin/specialists";
        }
    }


    @GetMapping("specialist/input")
    public String specialistInput() {
        return "admin/specialistInput";
    }

    @GetMapping("specialist/{id}/input")
    public String specialistIdInput(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("news", specialistService.getSpecialist(id));
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return "amdin/specialistInput";
        }
    }

    @PostMapping("specialist/edit{id}")
    public String specialistEdit(@Valid Specialist specialist,
                                 BindingResult result,
                                 @PathVariable("id") Long id,
                                 RedirectAttributes attributes) {
        if (id != null) {
            //修改
            Specialist specialist1 = specialistService.getSpecialist(id);
            if (specialist1 == null) {
                logger.error("数据库中不存在该记录");
                result.rejectValue("specialist","notFoundError", "数据库中不存在该记录");
            }
        }

        if (result.hasErrors()) {
            logger.info("传递参数有误:" + result.getFieldError().toString());

            String errorms = "";
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0; i < fieldErrors.size(); i++) {
                errorms += fieldErrors.get(i).getDefaultMessage() + "\n";
            }

            attributes.addFlashAttribute("message", errorms);

            if(id == null) {
                return "redirect:/admin/specialist/input";
            } else {
                String s = "redirect:/admin/specialist/" + id +"/input";
                return s;
            }
        }

        Specialist n = null;
        if (id == null) {
            n = specialistService.addSpecialist(specialist);
        } else {
            n = specialistService.updateSpecialist(id, specialist);
        }

        if (n == null) {
            //失败
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/specialists";

    }
}
