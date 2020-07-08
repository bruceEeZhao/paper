package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Aboutus {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @NotBlank(message = "内容不能为空")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    public Aboutus() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Aboutus{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
