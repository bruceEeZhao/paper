package com.ucas.paper.controller.admin;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.ucas.paper.entities.*;
import com.ucas.paper.service.JournalCNService;
import com.ucas.paper.service.JournalService;
import com.ucas.paper.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminJournalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Journal 部分
     * */

    @Autowired
    private JournalService journalService;

    @Autowired
    private TypeService typeService;

    //返回journal列表
    @GetMapping("journals")
    public String journaLlist(Pageable pageable, Model model,Integer page,
                              @RequestParam(value = "num", defaultValue = "20") Integer num) {
        if (page == null || page < 0){
            page = 0;
        }


        Sort order = Sort.by(Sort.Direction.DESC, "fms");
        pageable = PageRequest.of(page,num,order);

        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("page",journalService.listJournal(pageable));
        model.addAttribute("numb_show", num);
        return "admin/journalList";
    }

    @GetMapping("journal/input")
    public String journalInput(Model model,RedirectAttributes attributes) {
        List<Type> tmp = typeService.listType();
        if(tmp == null || tmp.isEmpty()) {
            attributes.addFlashAttribute("message", "请先添加学科");
            return "redirect:/admin/journals";
        }
        model.addAttribute("subjects", typeService.listType());
        return "admin/journalInput";
    }

    @GetMapping("journal/{id}/input")
    public String journalEditInput(@PathVariable("id")  Long id,
                                   Model model,
                                   RedirectAttributes attributes) {
        List<Type> tmp = typeService.listType();
        if(tmp == null || tmp.isEmpty()) {
            attributes.addFlashAttribute("message", "请先添加学科");
            return "redirect:/admin/journals";
        }
        model.addAttribute("subjects", typeService.listType());
        model.addAttribute("journal",journalService.getJournal(id));
        model.addAttribute("subject", journalService.getJournal(id).getSubject());
        return "admin/journalInput";
    }

    @PostMapping("journal/edit{id}")
    public String journalEdit(@Valid Journal journal,
                              BindingResult result,
                              @PathVariable("id") Long id,
                              RedirectAttributes attributes) {

        Journal journal1 = journalService.getJournalByIssn(journal.getIssn());

        if (id == null) {
            //新增
            if (journal1 != null) {
                result.rejectValue("issn", "nameError", "不允许重复的issn");
            }

            if (journal.getSubject() == null) {
                result.rejectValue("type", "nameError", "未选择学科");
            }

            List<Type> tmp = typeService.listType();
            if(tmp == null || tmp.isEmpty()) {
                result.rejectValue("type", "nameError", "请先添加学科");
            }
        } else {
            //修改
            if (journal1 != null && !journal1.getId().equals(id)) {
                result.rejectValue("issn", "nameError", "不允许重复的issn");
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
                return "redirect:/admin/journal/input";
            } else {
                String s = "redirect:/admin/journal/" + id +"/input";
                return s;
            }
        }

        journal.setSubject(journalService.getJournal(id).getSubject());

        Journal j = null;
        if (id == null) {
            j = journalService.addJournal(journal);
        } else {
            j = journalService.updateJournalById(id, journal);
        }
        if (j == null) {
            //失败
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/journals";
    }

    @GetMapping("journal/{id}/delete")
    public String journalDelete(@PathVariable("id") Long id,
                                RedirectAttributes attributes) {
        Journal journal1 = journalService.getJournal(id);
        if(journal1 == null) {
            logger.info("不存在该记录，不能删除");
            attributes.addFlashAttribute("message","不存在该记录，不能删除");

            return "redirect:/admin/journals";
        }

        try {
            journalService.deleteJournalById(id);
            attributes.addFlashAttribute("message", "删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            attributes.addFlashAttribute("message",e.toString());

        } finally {
            return "redirect:/admin/journals";
        }

    }

    /**
     * 清空数据
     * @return
     */
    @GetMapping("journal/delall")
    public String journalDelAll() {
        journalService.deleteAllJournal();
        return "redirect:/admin/journals";
    }


    @GetMapping("journal/upload")
    public String journalLoad() {
        return "admin/journalUpload";
    }

    @PostMapping("journal/upload")
    public String journalUpLoad(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        ImportParams importParams = new ImportParams();
        // 数据处理
//        importParams.setHeadRows(1);
//        importParams.setTitleRows(1);

        // 需要验证
        importParams.setNeedVerfiy(true);

        try {
            ExcelImportResult<JournalImport> result = ExcelImportUtil.importExcelMore(file.getInputStream(), JournalImport.class,
                    importParams);

            List<JournalImport> successList = result.getList();
            List<JournalImport> failList = result.getFailList();

            if (failList.size()>0) {
                attributes.addFlashAttribute("message", "成功" + successList.size() + "条,失败" + failList.size() + "条, 请检查数据格式");
            } else {
                attributes.addFlashAttribute("message", "成功" + successList.size() + "条,失败" + failList.size() + "条");
            }

            for (JournalImport journal : successList) {
                logger.info("成功列表" + journal.toString());
                //journalService.addJournal(journal);
                Journal j = ConvertToJournal(journal);
                if (j!=null) {
                    journalService.addJournal(j);
                }
            }

            for (JournalImport journal : failList) {
                logger.info("失败列表" + journal.toString());
                //journalService.addJournal(journal);
            }
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return "redirect:/admin/journals";
    }

    private Journal ConvertToJournal(JournalImport j) {
        List <Type> types = typeService.listType();
        Boolean flag = false;
        Type t = new Type();


        for (Type type :
                types) {
            if (type.getName().equals(j.getSubject())) {
                //已经存在该type
                flag=true;
                t.setId(type.getId());
                t.setName(type.getName());
            }
        }
        //不存在该type，创建
        if (!flag) {
            t.setName(j.getSubject());
            if (null == typeService.addType(t)) {
                return null;
            }
        }

       return new Journal(j.getIssn(),t,
                j.getName(), j.getFms(), j.getJcr(),
                j.getSjr(), j.getSnip());

    }


    /**
     * Journal CN 中文期刊
     */

    @Autowired
    private JournalCNService journalCNService;

    //返回journal列表
    @GetMapping("journals_cn")
    public String journaLlist_cn(Pageable pageable, Model model,Integer page,
                              @RequestParam(value = "num", defaultValue = "20") Integer num) {
        if (page == null || page < 0){
            page = 0;
        }

        Sort order = Sort.by(Sort.Direction.DESC, "fms");
        pageable = PageRequest.of(page,num,order);

        model.addAttribute("page",journalCNService.listJournal(pageable));
        model.addAttribute("numb_show", num);
        return "admin/journalList_cn";
    }

    @GetMapping("journal_cn/input")
    public String journalInput_cn(Model model,RedirectAttributes attributes) {

        return "admin/journalInput_cn";
    }

    @GetMapping("journal_cn/{id}/input")
    public String journalEditInput_cn(@PathVariable("id")  Long id,
                                   Model model,
                                   RedirectAttributes attributes) {
        model.addAttribute("journal",journalCNService.getJournal(id));

        return "admin/journalInput_cn";
    }

    @PostMapping("journal_cn/edit{id}")
    public String journalEdit(@Valid JournalCN journal,
                              BindingResult result,
                              @PathVariable("id") Long id,
                              RedirectAttributes attributes) {

        JournalCN journal1 = journalCNService.getJournalByIssn(journal.getIssn());

        if (id == null) {
            //新增
            if (journal1 != null) {
                result.rejectValue("issn", "nameError", "不允许重复的issn");
            }
        } else {
            //修改
            if (journal1 != null && !journal1.getId().equals(id)) {
                result.rejectValue("issn", "nameError", "不允许重复的issn");
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
                return "redirect:/admin/journal_cn/input";
            } else {
                String s = "redirect:/admin/journal_cn/" + id +"/input";
                return s;
            }
        }

        JournalCN j = null;
        if (id == null) {
            j = journalCNService.addJournal(journal);
        } else {
            j = journalCNService.updateJournalById(id, journal);
        }
        if (j == null) {
            //失败
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/journals_cn";
    }

    @GetMapping("journal_cn/{id}/delete")
    public String journalDelete_cn(@PathVariable("id") Long id,
                                RedirectAttributes attributes) {
        JournalCN journal1 = journalCNService.getJournal(id);
        if(journal1 == null) {
            logger.info("不存在该记录，不能删除");
            attributes.addFlashAttribute("message","不存在该记录，不能删除");

            return "redirect:/admin/journals_cn";
        }

        try {
            journalCNService.deleteJournalById(id);
            attributes.addFlashAttribute("message", "删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            attributes.addFlashAttribute("message",e.toString());

        } finally {
            return "redirect:/admin/journals_cn";
        }

    }

    /**
     * 清空数据
     * @return
     */
    @GetMapping("journal_cn/delall")
    public String journalDelAll_cn() {
        journalCNService.deleteAllJournal();
        return "redirect:/admin/journals_cn";
    }


    @GetMapping("journal_cn/upload")
    public String journalLoad_cn() {
        return "admin/journalUpload_cn";
    }

    @PostMapping("journal_cn/upload")
    public String journalUpLoad_cn(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        ImportParams importParams = new ImportParams();
        // 数据处理
//        importParams.setHeadRows(1);
//        importParams.setTitleRows(1);

        // 需要验证
        importParams.setNeedVerfiy(true);

        try {
            ExcelImportResult<JournalCNImport> result = ExcelImportUtil.importExcelMore(file.getInputStream(), JournalCNImport.class,
                    importParams);

            List<JournalCNImport> successList = result.getList();
            List<JournalCNImport> failList = result.getFailList();

            if (failList.size()>0) {
                attributes.addFlashAttribute("message", "成功" + successList.size() + "条,失败" + failList.size() + "条, 请检查数据格式");
            } else {
                attributes.addFlashAttribute("message", "成功" + successList.size() + "条,失败" + failList.size() + "条");
            }


            for (JournalCNImport journal : successList) {
                logger.info("成功列表" + journal.toString());
                //journalService.addJournal(journal);
                JournalCN j = ConvertToJournal_cn(journal);
                if (j!=null) {
                    journalCNService.addJournal(j);
                }
            }

            for (JournalCNImport journal : failList) {
                logger.info("失败列表" + journal.toString());
            }
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }



        return "redirect:/admin/journals_cn";
    }

    private JournalCN ConvertToJournal_cn(JournalCNImport j) {
        return new JournalCN(j.getIssn(),
                j.getName(), j.getFms(), j.getHost());

    }

}
