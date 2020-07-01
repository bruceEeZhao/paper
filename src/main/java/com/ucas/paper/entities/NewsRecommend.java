package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class NewsRecommend {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "图片地址不能为空")
    private String pic;

    @OneToOne
    private News nw;

    public NewsRecommend() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public News getNw() {
        return nw;
    }

    public void setNw(News nw) {
        this.nw = nw;
    }

    @Override
    public String toString() {
        return "NewsRecommend{" +
                "id=" + id +
                ", pic='" + pic + '\'' +
                ", nw=" + nw +
                '}';
    }
}
