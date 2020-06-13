package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.Type;
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
@RequestMapping("/admin")
public class AdminTypeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TypeService typeService;

    @GetMapping("types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("type/input")
    public String typeInput() {
        return "admin/typeInput";
    }

    @GetMapping("type/{id}/input")
    public String typeEditInput(@PathVariable("id") Long id,
                                Model model,
                                RedirectAttributes attributes) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/typeInput";
    }

    @GetMapping("type/{id}/delete")
    public String typeDel(@PathVariable("id") Long id,
                          RedirectAttributes attributes) {
        try {
            typeService.delType(id);
        } catch (Exception e) {
            attributes.addFlashAttribute("message",e);
        }

        return "redirect:/admin/types";
    }

    @PostMapping("type/edit{id}")
    public String typeEdit(@Valid Type type,
                           BindingResult result,
                           @PathVariable("id") Long id,
                           RedirectAttributes attributes) {
        try {
            Type type1 = typeService.getTypeByName(type.getName());

            if (id == null) {
                //新增
                if (type1 != null) {
                    result.rejectValue("type","","不能添加重复条目");
                }
            } else {
                //修改
                if (type1 == null) {
                    result.rejectValue("type","","数据库中不存在该记录");
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
                    return "redirect:/admin/type/input";
                } else {
                    String s = "redirect:/admin/type/" + id +"/input";
                    return s;
                }
            }

            Type j = null;
            if (id == null) {
                j = typeService.addType(type);
            } else {
                j = typeService.updateType(id, type);
            }
            if (j == null) {
                //失败
                attributes.addFlashAttribute("message","操作失败");
            } else {
                attributes.addFlashAttribute("message","操作成功");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("message",e);
        } finally {
            return "redirect:/admin/types";
        }
    }
}
