package models;

import java.io.*;
import java.util.*;

/**
 * Journal class using Observer pattern
 */
public class Journal implements Serializable {

    private String name;
    private List<Subscriber> subscribers;
    private List<ResearchPaper> publishedPapers;

    /**
     * Default constructor
     */
    public Journal() {
        this.subscribers = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    /**
     * Constructor with name
     */
    public Journal(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    public void publishPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        notifySubscribers(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(name, paper);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public List<ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    public void setPublishedPapers(List<ResearchPaper> publishedPapers) {
        this.publishedPapers = publishedPapers;
    }

    @Override
    public String toString() {
        return "Journal: " + name + " (" + publishedPapers.size() + " papers, " + subscribers.size() + " subscribers)";
    }

}