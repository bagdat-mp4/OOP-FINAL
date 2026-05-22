package models;

import enums.CitationFormat;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


public class ResearchPaper implements Serializable {

    private String title;
    private String journal;
    private String doi;
    private int pages;
    private int citations;
    private Date datePublished;
    private List<Researcher> authors;

    
    public ResearchPaper() {
        this.authors = new ArrayList<>();
    }

    
    public ResearchPaper(String title, String journal, String doi, int pages, Date datePublished) {
        this.title = title;
        this.journal = journal;
        this.doi = doi;
        this.pages = pages;
        this.datePublished = datePublished;
        this.citations = 0;
        this.authors = new ArrayList<>();
    }

    public int getCitations() {
        return citations;
    }

    public void setCitations(int citations) {
        this.citations = citations;
    }

    public String getCitation(CitationFormat format) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(datePublished);

        StringBuilder authorsStr = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (i > 0) authorsStr.append(", ");
            
            if (authors.get(i) instanceof ResearcherDecorator) {
                User u = ((ResearcherDecorator) authors.get(i)).getOriginalUser();
                authorsStr.append(u.getLastName()).append(" ").append(u.getFirstName().charAt(0)).append(".");
            }
        }

        if (format == CitationFormat.PLAIN_TEXT) {
            return authorsStr + " (" + year + "). " + title + ". " + journal + ". DOI: " + doi;
        } else if (format == CitationFormat.BIBTEX) {
            return "@article{" + doi + ",\n" +
                   "  title={" + title + "},\n" +
                   "  journal={" + journal + "},\n" +
                   "  year={" + year + "},\n" +
                   "  pages={" + pages + "},\n" +
                   "  doi={" + doi + "}\n" +
                   "}";
        }
        return "";
    }

    public void addAuthor(Researcher researcher) {
        authors.add(researcher);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public List<Researcher> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Researcher> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return title + " (" + citations + " citations, " + pages + " pages)";
    }

}