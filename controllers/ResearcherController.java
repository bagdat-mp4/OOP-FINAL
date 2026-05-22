package controllers;

import core.DataStore;
import models.User;
import models.ResearchPaper;
import models.ResearcherDecorator;
import models.ResearchProject;
import models.News;
import exceptions.NotAResearcherException;
import comparators.CitationsComparator;
import comparators.DateComparator;
import comparators.PaperLengthComparator;

import java.util.ArrayList;
import java.util.List;

public class ResearcherController {

    private final DataStore ds = DataStore.getInstance();

    public String validatePaper(ResearchPaper paper) {
        if (paper.getTitle() == null || paper.getTitle().isBlank()) return "Title cannot be empty.";
        if (paper.getJournal() == null || paper.getJournal().isBlank()) return "Journal cannot be empty.";
        if (paper.getDoi() == null || paper.getDoi().isBlank()) return "DOI cannot be empty.";
        if (paper.getPages() <= 0) return "Pages must be greater than 0.";
        if (paper.getCitations() < 0) return "Citations cannot be negative.";
        return "";
    }

    public boolean addResearchPaper(User author, ResearchPaper paper) {
        if (!ds.isResearcher(author)) return false;
        String error = validatePaper(paper);
        if (!error.isEmpty()) return false;
        ResearcherDecorator rd = ds.getResearcher(author);
        rd.addPaper(paper);
        paper.addAuthor(rd);

        ds.addNews(new News("New Research Paper Published", "Research",
            author.getFirstName() + " published: " + paper.getTitle()));
        ds.log(author, "Published paper: " + paper.getTitle());
        return true;
    }

    public ResearchProject createResearchProject(String topic) {
        ResearchProject p = new ResearchProject(topic);
        ds.addResearchProject(p);
        return p;
    }

    public boolean joinProject(ResearchProject project, User user) throws NotAResearcherException {
        if (!ds.isResearcher(user)) throw new NotAResearcherException("User is not a researcher.");
        project.addParticipant(user);
        ResearcherDecorator rd = ds.getResearcher(user);
        if (!rd.getProjects().contains(project)) {
            rd.addProject(project);
        }
        return true;
    }

    public double calculateHIndex(User user) {
        if (!ds.isResearcher(user)) return 0;
        return ds.getResearcher(user).calculateHIndex();
    }

    public List<ResearchPaper> getSortedPapers(List<ResearchPaper> papers, String criteria) {
        List<ResearchPaper> copy = new ArrayList<>(papers);
        if (criteria.equals("citations")) {
            copy.sort(new CitationsComparator());
        } else if (criteria.equals("date")) {
            copy.sort(new DateComparator());
        } else {
            copy.sort(new PaperLengthComparator());
        }
        return copy;
    }
}
