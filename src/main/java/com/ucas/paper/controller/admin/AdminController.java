package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.*;
import com.ucas.paper.handler.PasswordHandler;
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


    @GetMapping("edit")
    public String adminEdit() {
        return "admin/adminEdit";
    }

    @PostMapping("editp")
    public String adminEditPassword(HttpSession session,RedirectAttributes attributes,
                                    @RequestParam("Opassword") String opwd,
                                    @RequestParam("Npassword1") String npwd1,
                                    @RequestParam("Npassword2") String npwd2) {
        if (opwd.isEmpty() || npwd1.isEmpty() || npwd2.isEmpty()) {
            attributes.addFlashAttribute("message", "密码不能为空");
            return "redirect:/admin/edit";
        }

        if (!npwd1.equals(npwd2)) {
            attributes.addFlashAttribute("message", "两次密码输入不一致");
            return "redirect:/admin/edit";
        }

        if(!PasswordHandler.checkPassword(npwd1)) {
            attributes.addFlashAttribute("message", "密码规则：包含数字，大写字母，小写字母，特殊符号,长度大于6小于128");
            return "redirect:/admin/edit";
        }


        if (userService.checkUser("admin", opwd) == null) {
            attributes.addFlashAttribute("message", "旧密码错误");
            return "redirect:/admin/edit";
        }


        //验证正确，修改密码,修改完成后重新登录

        if(userService.upDateUser("admin", opwd, npwd1)) {
            session.removeAttribute("loginUser");
            attributes.addFlashAttribute("message", "密码修改成功，请重新登录");
            return "redirect:/admin";
        } else {
            attributes.addFlashAttribute("message", "密码修改失败");
            return "redirect:/admin/edit";
        }

    }




    /**
     * specialist
     */


    /**
     * ip
     */

    /**
     * aboutus
     */
    @Autowired
    private AboutusService aboutusService;

    @Autowired
    private PurposeService purposeService;

    @GetMapping("aboutus")
    public String aboutusList(Model model) {

        model.addAttribute("about", aboutusService.getAboutus());
        model.addAttribute("purpose", purposeService.getPurpose());
        return "admin/aboutus";
    }

    @PostMapping("aboutus/edit")
    public String aboutusEdit(@Valid Aboutus about,
                              BindingResult result,
                              RedirectAttributes attributes){


        aboutusService.updateAboutus(about);


        if (result.hasErrors()) {
            logger.info("传递参数有误:" + result.getFieldError().toString());

            String errorms = "";
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0; i < fieldErrors.size(); i++) {
                errorms += fieldErrors.get(i).getDefaultMessage() + "\n";
            }

            attributes.addFlashAttribute("message", errorms);
        }

        return  "redirect:/admin/aboutus";
    }

    @PostMapping("purpose/edit")
    public String purposeEdit(@Valid Purpose purpose,
                              BindingResult result,
                              RedirectAttributes attributes){

        purposeService.updatePurpose(purpose);

        if (result.hasErrors()) {
            logger.info("传递参数有误:" + result.getFieldError().toString());

            String errorms = "";
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (int i = 0; i < fieldErrors.size(); i++) {
                errorms += fieldErrors.get(i).getDefaultMessage() + "\n";
            }

            attributes.addFlashAttribute("message", errorms);
        }

        return  "redirect:/admin/aboutus";
    }
}
