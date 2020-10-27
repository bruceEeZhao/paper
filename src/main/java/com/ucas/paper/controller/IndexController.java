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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
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

    //英文版首页
    @GetMapping(value = {"/english"})
    public String indexEnglish(Model model) {
        Purpose purpose = purposeService.getAndConvert();

        model.addAttribute("purpose", purpose);

        return "index_english";
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
        if (journalPdf == null) {
            return "管理员未上传,下载失败";
        }


        String fileName = journalPdf.getFilename();
        if (fileName != null) {
            String type = fileName.substring(fileName.indexOf("."));
            //设置文件路径
            File file = new File(fileName);
            if (file.exists()) {
                //get the mimetype
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    //unknown mimetype so set the mimetype to application/octet-stream
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);

                /**
                 * In a regular HTTP response, the Content-Disposition response header is a
                 * header indicating if the content is expected to be displayed inline in the
                 * browser, that is, as a Web page or as part of a Web page, or as an
                 * attachment, that is downloaded and saved locally.
                 *
                 */

                /**
                 * Here we have mentioned it to attachment
                 */
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + "global" + type + "\""));

                //Here we have mentioned it to show as attachment
                //response.setHeader("Content-Disposition", String.format("inline; filename=\"" + "global" + type + "\""));

                response.setContentLength((int) file.length());

                InputStream inputStream = null;
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    flag = 1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    logger.error(e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.toString());
                }
            }
        }
        if (flag == 1) {
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
            String type = fileName.substring(fileName.indexOf("."));
            //设置文件路径
            File file = new File(fileName);
            if (file.exists()) {
                //get the mimetype
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    //unknown mimetype so set the mimetype to application/octet-stream
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);

                /**
                 * In a regular HTTP response, the Content-Disposition response header is a
                 * header indicating if the content is expected to be displayed inline in the
                 * browser, that is, as a Web page or as part of a Web page, or as an
                 * attachment, that is downloaded and saved locally.
                 *
                 */

                /**
                 * Here we have mentioned it to attachment
                 */
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + "chinese" + type + "\""));

                //Here we have mentioned it to show as attachment
                //response.setHeader("Content-Disposition", String.format("inline; filename=\"" + "global" + type +  "\""));

                response.setContentLength((int) file.length());

                InputStream inputStream = null;
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                    FileCopyUtils.copy(inputStream, response.getOutputStream());
                    flag = 1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    logger.error(e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.toString());
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
