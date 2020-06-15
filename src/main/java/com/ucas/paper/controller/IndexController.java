package com.ucas.paper.controller;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.entities.Specialist;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.NewsService;
import com.ucas.paper.service.SpecialistService;
import com.ucas.paper.service.TypeService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


@Controller
public class IndexController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private SpecialistService specialistService;

    @GetMapping(value = {"/", "/index.html"})
    public String index(Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        model.addAttribute("specialists", specialistService.listSpecialist());
        return "index";
    }

    @GetMapping("/aboutus")
    public String aboutus(Model model) {
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        return "aboutus";
    }

    @GetMapping("/download")
    public void downLoad(HttpServletResponse response) throws UnsupportedEncodingException {
        XSSFWorkbook wb = journalService.show();
        String fileName = "journal.xlsx";
        OutputStream outputStream =null;
        try {
            fileName = URLEncoder.encode(fileName,"UTF-8");
            //设置ContentType请求信息格式
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
