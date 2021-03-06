package com.ucas.paper.entities;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.NotNull;

public class JournalImport {

    @Excel(name = "issn", orderNum = "0")
    @NotNull
    private String issn;

    @Excel(name = "学科", orderNum = "1")
    @NotNull
    private String subject;

    @Excel(name = "英文学科", orderNum = "2")
    @NotNull
    private String subjectEng;

    @Excel(name = "期刊名称", orderNum = "3")
    @NotNull
    private String name;  //期刊名称

    @Excel(name = "fms", orderNum = "4")
    private String fms;

    @Excel(name = "jcr", orderNum = "5")
    private Integer jcr;

    @Excel(name = "sjr", orderNum = "6")
    private Integer sjr;

    @Excel(name = "snip", orderNum = "7")
    private Integer snip;


    public JournalImport() {
    }

    public String getSubjectEng() {
        return subjectEng;
    }

    public void setSubjectEng(String subjectEng) {
        this.subjectEng = subjectEng;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFms() {
        return fms;
    }

    public void setFms(String fms) {
        this.fms = fms;
    }

    public Integer getJcr() {
        return jcr;
    }

    public void setJcr(Integer jcr) {
        this.jcr = jcr;
    }

    public Integer getSjr() {
        return sjr;
    }

    public void setSjr(Integer sjr) {
        this.sjr = sjr;
    }

    public Integer getSnip() {
        return snip;
    }

    public void setSnip(Integer snip) {
        this.snip = snip;
    }

    @Override
    public String toString() {
        return "JournalImport{" +
                "issn='" + issn + '\'' +
                ", subject='" + subject + '\'' +
                ", subjectEng='" + subjectEng + '\'' +
                ", name='" + name + '\'' +
                ", fms='" + fms + '\'' +
                ", jcr=" + jcr +
                ", sjr=" + sjr +
                ", snip=" + snip +
                '}';
    }
}
