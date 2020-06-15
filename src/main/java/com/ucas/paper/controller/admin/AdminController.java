package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.Journal;
import com.ucas.paper.entities.News;
import com.ucas.paper.entities.User;
import com.ucas.paper.service.*;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private SpecialistService specialistService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("dashboard")
    public String dashboard(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                        Pageable pageable,Model model) {
        model.addAttribute("subject", typeService.count());
        model.addAttribute("journal",journalService.count());
        model.addAttribute("news", newsService.count());
        model.addAttribute("specialist",specialistService.count());
        return "admin/dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("loginUser", user);
            return "redirect:/admin/dashboard";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("logout")
    public String Logout(HttpSession session) {
        session.removeAttribute("loginUser");
        return  "redirect:/admin";
    }






    /**
     * specialist
     */


    /**
     * ip
     */
}
