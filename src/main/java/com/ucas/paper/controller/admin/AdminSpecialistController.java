package com.ucas.paper.controller.admin;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.entities.Specialist;
import com.ucas.paper.service.SpecialistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class AdminSpecialistController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**上传地址*/
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.mappath}")
    private String mapPath;

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
            Specialist specialist = specialistService.getSpecialist(id);
            if (specialist == null) {
                throw new NotFoundException("未找到该人员");
            }
            model.addAttribute("filename", specialist.getPhoto());
            model.addAttribute("specialist", specialist);
        } catch (Exception e) {
            logger.error(e.toString());
            model.addAttribute("message",e.toString());
        } finally {
            return "admin/specialistInput";
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


    // 执行上传
    @PostMapping("specialist/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model,@RequestParam("id") Long id) {
        if (file==null) {
            model.addAttribute("message", "没有选择文件");
            return "/admin/specialistInput";
        }
        if (id!=null) {
            model.addAttribute("specialist",specialistService.getSpecialist(id));
        }
        //获取原文件的文件名
        try {
            String oldName=file.getOriginalFilename();
            logger.info(oldName);

            if (oldName==null || "".equals(oldName)) {
                model.addAttribute("message", "没有选择文件");
                return "/admin/specialistInput";
            }
            //原文件的类型
            String type=oldName.substring(oldName.indexOf(".")); // 格式为.jpg 或 .png 或 ......

            //将文件名修改为时间戳，避免原文件出现文件名过长情况
            String filename = "/FILE"+new Date().getTime()+type;

            // 如果目录不存在则创建
            File tempFile=new File(filePath+filename);


        if (!tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdirs();//创建父级文件路径
            tempFile.createNewFile();//创建文件
        }
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(tempFile);
        logger.info(tempFile.getName());

        model.addAttribute("filename", mapPath+"/"+filename);
        model.addAttribute("message", "上传成功");

        } catch (Exception e) {
            model.addAttribute("message", "上传失败:"+e.toString());
            return "/admin/specialistInput";
        }

        return "/admin/specialistInput";
    }


    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


}
