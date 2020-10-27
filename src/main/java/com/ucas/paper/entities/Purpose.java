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

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    //英语版的推荐
    private String titleEng;
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String contentEng;

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

    public String getTitleEng() {
        return titleEng;
    }

    public void setTitleEng(String titleEng) {
        this.titleEng = titleEng;
    }

    public String getContentEng() {
        return contentEng;
    }

    public void setContentEng(String contentEng) {
        this.contentEng = contentEng;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", titleEng='" + titleEng + '\'' +
                ", contentEng='" + contentEng + '\'' +
                '}';
    }
}
