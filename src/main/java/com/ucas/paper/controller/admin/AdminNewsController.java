package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.News;
import com.ucas.paper.service.NewsService;
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
public class AdminNewsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * news
     * */

    @Autowired
    private NewsService newsService;

    @GetMapping("news")
    public String newsList(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                       Pageable pageable, Model model) {
        try {
            model.addAttribute("page",newsService.listNews(pageable));
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return "admin/news";
        }
    }

    @GetMapping("news/input")
    public String newsInput() {
        return "admin/newsInput";
    }

    @GetMapping("news/{id}/input")
    public String newsIdInput(@PathVariable("id")  Long id, Model model) {
        try {
            model.addAttribute("news", newsService.getNews(id));
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return "admin/newsInput";
        }
    }


    /**
     * 编辑新闻，新增新闻
     * @param news
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("news/edit{id}")
    public String newsEdit(@Valid News news,
                           BindingResult result,
                           @PathVariable("id") Long id,
                           RedirectAttributes attributes) {

        if (id != null) {
            //修改
            News news1 = newsService.getNews(id);
            if (news1 == null) {
                logger.error("数据库中不存在该记录");
                result.rejectValue("news","notFoundError", "数据库中不存在该记录");
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
                return "redirect:/admin/news/input";
            } else {
                String s = "redirect:/admin/news/" + id +"/input";
                return s;
            }
        }

        News n = null;
        if (id == null) {
            n = newsService.addNews(news);
        } else {
            n = newsService.updateNewsById(id, news);
        }

        if (n == null) {
            //失败
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/news";

    }


    /**
     * 删除新闻
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("news/{id}/delete")
    public String delNews(@PathVariable("id") Long id,RedirectAttributes attributes) {
        News n = newsService.getNews(id);
        if(n == null) {
            logger.info("不存在该记录，不能删除");
            attributes.addFlashAttribute("message","不存在该记录，不能删除");

            return "redirect:/admin/news";
        }

        try {
            newsService.delNews(id);
            attributes.addFlashAttribute("message", "删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            attributes.addFlashAttribute("message",e.toString());
        }
        finally {
            return "redirect:/admin/news";
        }
    }

}
