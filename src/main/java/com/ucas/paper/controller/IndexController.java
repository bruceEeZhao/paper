package com.ucas.paper.controller;

import com.ucas.paper.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping("/aboutus")
    public String aboutus() {
        return "aboutus";
    }
}
