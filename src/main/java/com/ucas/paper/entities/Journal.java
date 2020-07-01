package com.ucas.paper.entities;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "issn不能为空")
    private String issn;
    //private String subject; //学科

    @ManyToOne
    private Type subject;

    @Column(nullable = false)
    @NotBlank(message = "期刊名称不能为空")
    private String name;  //期刊名称

    private String fms;

    private Integer jcr;

    private Integer sjr;

    private Integer snip;

    private Integer ipp;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Journal() {
    }

    public Journal(@NotBlank(message = "issn不能为空") String issn, Type subject, @NotBlank(message = "期刊名称不能为空") String name, String fms, Integer jcr, Integer sjr, Integer snip, Integer ipp) {
        this.issn = issn;
        this.subject = subject;
        this.name = name;
        this.fms = fms;
        this.jcr = jcr;
        this.sjr = sjr;
        this.snip = snip;
        this.ipp = ipp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Type getSubject() {
        return subject;
    }

    public String getSubjectName() { return subject.getName(); }

    public void setSubject(Type subject) {
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

    public Integer getIpp() {
        return ipp;
    }

    public void setIpp(Integer ipp) {
        this.ipp = ipp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", issn='" + issn + '\'' +
                ", subject='" + subject + '\'' +
                ", name='" + name + '\'' +
                ", fms='" + fms + '\'' +
                ", jcr=" + jcr +
                ", sjr=" + sjr +
                ", snip=" + snip +
                ", ipp=" + ipp +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }


}
