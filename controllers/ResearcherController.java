package controllers;

import core.DataStore;
import models.*;
import exceptions.*;
import comparators.*;

import java.util.*;

/**
 * Researcher controller
 */
public class ResearcherController {

    public ResearcherController() {
    }

    public boolean addResearchPaper(User author, ResearchPaper paper) {
        DataStore ds = DataStore.getInstance();
        if (!ds.isResearcher(author)) return false;
        ResearcherDecorator rd = ds.getResearcher(author);
        rd.addPaper(paper);
        paper.addAuthor(rd);

        // Auto-create news for research papers
        News news = new News("New Research Paper Published", "Research",
            author.getFirstName() + " published: " + paper.getTitle());
        ds.addNews(news);
        ds.log(author, "Published paper: " + paper.getTitle());
        return true;
    }

    public ResearchProject createResearchProject(String topic) {
        ResearchProject p = new ResearchProject(topic);
        DataStore.getInstance().addResearchProject(p);
        return p;
    }

    public boolean joinProject(ResearchProject project, User user) throws NotAResearcherException {
        project.addParticipant(user);
        return true;
    }

    public double calculateHIndex(User user) {
        DataStore ds = DataStore.getInstance();
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
            copy.sort(new PaperLengthComporator());
        }
        return copy;
    }

}
