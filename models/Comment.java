package models;

import java.io.*;
import java.util.*;

/**
 * Comment class for news comments
 */
public class Comment implements Serializable {

    private User author;
    private String text;

    /**
     * Default constructor
     */
    public Comment() {
    }

    /**
     * Constructor with parameters
     */
    public Comment(User author, String text) {
        this.author = author;
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return author.getFirstName() + ": " + text;
    }

}