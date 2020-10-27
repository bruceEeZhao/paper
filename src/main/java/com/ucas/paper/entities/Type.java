package com.ucas.paper.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;

    @NotBlank
    private String engName;

    @OneToMany(mappedBy = "subject")
    private List<Journal> journals = new ArrayList<Journal>();


    public Type() {
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

    public List<Journal> getJournals() {
        return journals;
    }

    public void setJournals(List<Journal> journals) {
        this.journals = journals;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", engName='" + engName + '\'' +
                ", journals=" + journals +
                '}';
    }
}
