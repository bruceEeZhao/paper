package com.ucas.paper.entities;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.NotNull;

public class JournalCNImport {

    @Excel(name = "issn", orderNum = "0")
    @NotNull
    private String issn;

    @Excel(name = "期刊名称", orderNum = "1")
    @NotNull
    private String name;  //期刊名称

    @Excel(name = "fms", orderNum = "2")
    @NotNull
    private String fms;

    @Excel(name = "主办单位", orderNum = "3")
    @NotNull
    private String host;

    public JournalCNImport() {
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "JournalCNImport{" +
                "issn='" + issn + '\'' +
                ", name='" + name + '\'' +
                ", fms='" + fms + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
