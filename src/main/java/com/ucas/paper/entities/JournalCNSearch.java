package com.ucas.paper.entities;

public class JournalCNSearch {
    private String issn;
    private String name;

    public JournalCNSearch() {
    }

    public JournalCNSearch(String issn, String name) {
        this.issn = issn;
        this.name = name;
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
        return "JournalCNSearch{" +
                "issn='" + issn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
