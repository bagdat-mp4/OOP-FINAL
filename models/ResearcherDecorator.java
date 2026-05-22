package models;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class ResearcherDecorator implements Researcher, Serializable {

    private User originalUser;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    
    public ResearcherDecorator() {
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    
    public ResearcherDecorator(User user) {
        this.originalUser = user;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    @Override
    public double calculateHIndex() {
        if (papers.isEmpty()) return 0.0;

        List<Integer> citationsSorted = papers.stream()
            .map(ResearchPaper::getCitations)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());

        int h = 0;
        for (int i = 0; i < citationsSorted.size(); i++) {
            if (citationsSorted.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        for (ResearchPaper p : sorted) {
            System.out.println(p);
        }
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public void addPaper(ResearchPaper researchPaper) {
        papers.add(researchPaper);
    }

    public void addProject(ResearchProject researchProject) {
        projects.add(researchProject);
    }

    public List<ResearchProject> getProjects() {
        return projects;
    }

    public User getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(User originalUser) {
        this.originalUser = originalUser;
    }

}