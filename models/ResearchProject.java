package models;

import core.DataStore;
import exceptions.NotAResearcherException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResearchProject implements Serializable {

    private String topic;
    private List<Researcher> participants;
    private List<ResearchPaper> publishedPapers;


    public ResearchProject() {
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public ResearchProject(String topic) {
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public void addParticipant(User user) throws NotAResearcherException {
        DataStore ds = DataStore.getInstance();
        if (!ds.isResearcher(user)) {
            throw new NotAResearcherException("User " + user.getFirstName() + " is not a researcher!");
        }
        ResearcherDecorator rd = ds.getResearcher(user);
        if (!participants.contains(rd)) {
            participants.add(rd);
        }
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public List<Researcher> getParticipants() { return participants; }
    public void setParticipants(List<Researcher> participants) { this.participants = participants; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }
    public void setPublishedPapers(List<ResearchPaper> publishedPapers) { this.publishedPapers = publishedPapers; }

    @Override
    public String toString() {
        return "Project: " + topic + " (" + participants.size() + " participants, " + publishedPapers.size() + " papers)";
    }
}
