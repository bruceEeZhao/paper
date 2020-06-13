package com.ucas.paper.entities;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "statistic")
public class Statistic {
    @Id
    @GeneratedValue
    private Long id;
    private String ip;
    private String page;
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date eixtTime;

    public Statistic() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Date getEixtTime() {
        return eixtTime;
    }

    public void setEixtTime(Date eixtTime) {
        this.eixtTime = eixtTime;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "ip='" + ip + '\'' +
                ", page='" + page + '\'' +
                ", visitTime=" + visitTime +
                ", eixtTime=" + eixtTime +
                '}';
    }
}
