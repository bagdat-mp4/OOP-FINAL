package models;

import java.io.*;
import java.util.*;

/**
 * News class representing university news
 */
public class News implements Serializable {

    private String topic;
    private String content;
    private boolean isPinned;
    private List<Comment> comments;
    private String title;

    /**
     * Default constructor
     */
    public News() {
        this.comments = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public News(String title, String topic, String content) {
        this.title = title;
        this.topic = topic;
        this.content = content;
        this.comments = new ArrayList<>();
        // Auto-pin if topic is Research
        if (topic.equalsIgnoreCase("Research")) {
            this.isPinned = true;
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        String prefix = isPinned ? "📌 [PINNED] " : "";
        return prefix + "[" + topic + "] " + title + ": " + content;
    }

}