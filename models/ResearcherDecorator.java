package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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

        List<Integer> sorted = new ArrayList<>();
        for (ResearchPaper p : papers) sorted.add(p.getCitations());
        sorted.sort(Comparator.reverseOrder());

        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        sorted.forEach(System.out::println);
    }

    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    public void addPaper(ResearchPaper researchPaper) { papers.add(researchPaper); }
    public void addProject(ResearchProject researchProject) { projects.add(researchProject); }
    public List<ResearchProject> getProjects() { return projects; }
    public User getOriginalUser() { return originalUser; }
    public void setOriginalUser(User originalUser) { this.originalUser = originalUser; }
}
