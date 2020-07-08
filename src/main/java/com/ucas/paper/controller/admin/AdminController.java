package com.ucas.paper.controller.admin;

import com.ucas.paper.entities.*;
import com.ucas.paper.handler.PasswordHandler;
import com.ucas.paper.service.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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

    @Autowired
    private JournalCNService journalCNService;


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


        if (about.getTitle()==null || "".equals(about.getTitle().trim())) {
            about.setTitle("关于我们");
        }
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

        if (purpose.getTitle()==null || "".equals(purpose.getTitle().trim())) {
            purpose.setTitle("期刊列表推荐宗旨");
        }

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


    @GetMapping("download")
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

    @GetMapping("download_cn")
    public void downLoad_cn(HttpServletResponse response) throws UnsupportedEncodingException {
        XSSFWorkbook wb = journalCNService.show();
        String fileName = "journal_cn.xlsx";
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


    /**上传地址*/
    @Value("${file.upload.pdfpath}")
    private String filePath;

    @Value("${file.pdfmap}")
    private String mapPath;

    @Autowired
    private JournalPdfService journalPdfService;

    @GetMapping("journals_pdf")
    public String journals_pdf_upload(Model model) {
        model.addAttribute("journal", journalPdfService.getByName("国际期刊"));
        return "admin/journal_pdf_upload";
    }

    // 执行上传
    @PostMapping("journals_pdf")
    public String journals_pdf_upload(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
                                      @RequestParam("alias") String alias)  {
        if (file==null) {
            attributes.addFlashAttribute("message", "没有选择文件");
            return "redirect:/admin/journals_pdf";
        }

        //获取原文件的文件名
        try {
            String oldName=file.getOriginalFilename();
            logger.info(oldName);

            if (oldName==null || "".equals(oldName)) {
                attributes.addFlashAttribute("message", "没有选择文件");
                return "redirect:/admin/journals_pdf";
            }
            //原文件的类型
            String type=oldName.substring(oldName.indexOf(".")); // 文件格式

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
            logger.info(filePath+filename);

            JournalPdf journalPdf = new JournalPdf();
            journalPdf.setName("国际期刊");
            journalPdf.setFilename(filePath+filename);
            if (alias==null || "".equals(alias.trim())) {
                journalPdf.setAlias("FMS Journal Rating Guide（国际期刊）");
            } else {
                journalPdf.setAlias(alias);
            }
            journalPdfService.upDate(journalPdf);

            attributes.addFlashAttribute("filename", mapPath+"/"+filename);
            attributes.addFlashAttribute("message", oldName+"上传成功");

        } catch (Exception e) {
            attributes.addFlashAttribute("message", "上传失败:"+e.toString());
            return "redirect:/admin/journals_pdf";
        }

        return "redirect:/admin/journals_pdf";
    }

    @GetMapping("journals_cn_pdf")
    public String journals_cn_pdf_upload(Model model) {
        model.addAttribute("journal", journalPdfService.getByName("中文期刊"));
        return "admin/journal_cn_pdf_upload";
    }

    // 执行上传
    @PostMapping("journals_cn_pdf")
    public String journals_cn_pdf_upload(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
                                         @RequestParam("alias") String alias)  {
        if (file==null) {
            attributes.addFlashAttribute("message", "没有选择文件");
            return "redirect:/admin/journals_cn_pdf";
        }
        //获取原文件的文件名
        try {
            String oldName=file.getOriginalFilename();
            logger.info(oldName);

            if (oldName==null || "".equals(oldName)) {
                attributes.addFlashAttribute("message", "没有选择文件");
                return "redirect:/admin/journals_cn_pdf";
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
            logger.info(mapPath+"/"+filename);

            JournalPdf journalPdf = new JournalPdf();
            journalPdf.setName("中文期刊");
            journalPdf.setFilename(filePath+filename);
            if (alias==null || "".equals(alias.trim())) {
                journalPdf.setAlias("FMS管理科学高质量期刊（中文期刊）");
            } else {
                journalPdf.setAlias(alias);
            }
            journalPdfService.upDate(journalPdf);

            attributes.addFlashAttribute("filename", mapPath+"/"+filename);
            attributes.addFlashAttribute("message", oldName+"上传成功");

        } catch (Exception e) {
            attributes.addFlashAttribute("message", "上传失败:"+e.toString());
            return "redirect:/admin/journals_cn_pdf";
        }

        return "redirect:/admin/journals_cn_pdf";
    }


}
