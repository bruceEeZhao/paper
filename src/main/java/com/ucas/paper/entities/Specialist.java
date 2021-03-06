package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "specialist")
public class Specialist {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "简介不能为空")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String brief; //简介
    @NotBlank(message = "图片未上传")
    private String photo;

    public Specialist() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Specialist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brief='" + brief + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
