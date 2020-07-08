package com.ucas.paper.controller;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.entities.Aboutus;
import com.ucas.paper.entities.JournalPdf;
import com.ucas.paper.entities.Purpose;
import com.ucas.paper.entities.Specialist;
import com.ucas.paper.service.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;


@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewsService newsService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private JournalCNService journalCNService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private AboutusService aboutusService;

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private NewsReService newsReService;

    @GetMapping(value = {"/", "/index.html", "/index"})
    public String index(Model model) {
        Purpose purpose = purposeService.getAndConvert();


        model.addAttribute("purpose", purpose);
        model.addAttribute("news",newsService.listPublishedNesw(5,0));
        model.addAttribute("newr", newsReService.getNewRe());

        model.addAttribute("global", journalPdfService.getByName("国际期刊"));
        model.addAttribute("chinese", journalPdfService.getByName("中文期刊"));
        return "index";
    }

    @GetMapping("/aboutus")
    public String aboutus(Model model) {

        model.addAttribute("about", aboutusService.getAndConvert());
        model.addAttribute("newr", newsReService.getNewRe());
        model.addAttribute("news",newsService.listPublishedNesw(5,0));

        return "aboutus";
    }

    @Autowired
    private JournalPdfService journalPdfService;


    /**上传地址*/
    @Value("${file.upload.pdfpath}")
    private String filePath;

    @Value("${file.pdfmap}")
    private String mapPath;
    /**
     * downloadpdf
     * @param response
     * @return
     */
    @ResponseBody
    @GetMapping("download_pdf")
    public String downloadFilepdf(HttpServletResponse response) {

        Integer flag = 0;

        JournalPdf journalPdf = journalPdfService.getByName("国际期刊");
        if (journalPdf==null) {
            return "管理员未上传,下载失败";
        }


        String fileName = journalPdf.getFilename();
        if (fileName != null) {
            String type=fileName.substring(fileName.indexOf("."));
            //设置文件路径
            File file = new File(fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + "global"+type);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }

                    //attributes.addFlashAttribute("message", "下载成功");
                    flag = 1;
                } catch (Exception e) {
                    //attributes.addFlashAttribute("message", "下载失败");
                    logger.error(e.toString());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (flag==1) {
            return "下载成功";
        } else {
            return "下载失败";
        }
    }

    @ResponseBody
    @GetMapping("download_cn_pdf")
    public String downloadFilecnpdf(HttpServletResponse response) {

        Integer flag = 0;

        JournalPdf journalPdf = journalPdfService.getByName("中文期刊");
        if (journalPdf==null) {
            return "管理员未上传,下载失败";
        }

        String fileName = journalPdf.getFilename();
        if (fileName != null) {
            //设置文件路径
            File file = new File(fileName);
            if (file.exists()) {
                String type = fileName.substring(fileName.indexOf("."));
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + "chinese"+type);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }

                    flag = 1;

                } catch (Exception e) {
                    logger.error(e.toString());
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (flag==1) {
            return "下载成功";
        } else {
            return "下载失败";
        }
    }

}
