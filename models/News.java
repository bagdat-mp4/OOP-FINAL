package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class News implements Serializable {

    private String title;
    private String topic;
    private String content;
    private boolean pinned;
    private List<Comment> comments;


    public News() {
        this.comments = new ArrayList<>();
    }

    public News(String title, String topic, String content) {
        this.title = title;
        this.topic = topic;
        this.content = content;
        this.comments = new ArrayList<>();
        this.pinned = topic.equalsIgnoreCase("Research");
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    @Override
    public String toString() {
        String prefix = pinned ? " [PINNED] " : "";
        return prefix + "[" + topic + "] " + title + ": " + content;
    }
}
