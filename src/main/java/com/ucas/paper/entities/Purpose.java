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
    @NotBlank(message = "内容不能为空")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    public Purpose() {
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
                ", content='" + content + '\'' +
                '}';
    }
}
