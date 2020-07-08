package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * 期刊宗旨
 * */
@Entity
public class Purpose {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @NotBlank(message = "内容不能为空")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    public Purpose() {
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
        return "Purpose{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
