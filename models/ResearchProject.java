package models;

import core.DataStore;
import exceptions.NotAResearcherException;

import java.io.*;
import java.util.*;


public class ResearchProject implements Serializable {

    private String topic;
    private List<models.Researcher> participants;
    private List<models.ResearchPaper> publishedPapers;

    
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
        models.ResearcherDecorator rd = ds.getResearcher(user);
        participants.add(rd);
    }

    public void addPaper(models.ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<models.Researcher> getParticipants() {
        return participants;
    }

    public void setParticipants(List<models.Researcher> participants) {
        this.participants = participants;
    }

    public List<models.ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    public void setPublishedPapers(List<models.ResearchPaper> publishedPapers) {
        this.publishedPapers = publishedPapers;
    }

    @Override
    public String toString() {
        return "Project: " + topic + " (" + participants.size() + " participants, " + publishedPapers.size() + " papers)";
    }

}