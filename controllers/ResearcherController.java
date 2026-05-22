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
import comparators.PaperLengthComporator;

import java.util.List;
import java.util.ArrayList;

public class ResearcherController {

    private final DataStore ds = DataStore.getInstance();

    public ResearcherController() {
    }

    public boolean addResearchPaper(User author, ResearchPaper paper) {
        if (!ds.isResearcher(author)) return false;
        ResearcherDecorator rd = ds.getResearcher(author);
        rd.addPaper(paper);
        paper.addAuthor(rd);

        News news = new News("New Research Paper Published", "Research",
            author.getFirstName() + " published: " + paper.getTitle());
        ds.addNews(news);
        ds.log(author, "Published paper: " + paper.getTitle());
        return true;
    }

    public ResearchProject createResearchProject(String topic) {
        ResearchProject p = new ResearchProject(topic);
        ds.addResearchProject(p);
        return p;
    }

    public boolean joinProject(ResearchProject project, User user) throws NotAResearcherException {
        project.addParticipant(user);
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
            copy.sort(new PaperLengthComporator());
        }
        return copy;
    }

}
