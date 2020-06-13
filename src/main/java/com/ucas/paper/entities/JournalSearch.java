package com.ucas.paper.entities;

public class JournalSearch {
    private Long subjectId;
    private String issn;
    private String name;

    public JournalSearch() {
    }

    public JournalSearch(Long subjectId, String issn, String name) {
        this.subjectId = subjectId;
        this.issn = issn;
        this.name = name;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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

    @Override
    public String toString() {
        return "JournalSearch{" +
                "subjectId=" + subjectId +
                ", issn='" + issn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
