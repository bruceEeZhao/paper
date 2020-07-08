package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class JournalCN {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "issn不能为空")
    private String issn;

    @Column(nullable = false)
    @NotBlank(message = "期刊名称不能为空")
    private String name;  //期刊名称

    private String fms;

    @Column(nullable = false)
    @NotBlank(message = "主办单位不能为空")
    private String host;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public JournalCN() {
    }

    public JournalCN( @NotBlank(message = "issn不能为空") String issn, @NotBlank(message = "期刊名称不能为空") String name, String fms, String host) {
        this.issn = issn;
        this.name = name;
        this.fms = fms;
        this.host = host;
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
        return "JournalCN{" +
                "id=" + id +
                ", issn='" + issn + '\'' +
                ", name='" + name + '\'' +
                ", fms='" + fms + '\'' +
                ", host='" + host + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
