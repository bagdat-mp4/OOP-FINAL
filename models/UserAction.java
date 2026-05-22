package models;

import java.io.*;
import java.util.*;


public class UserAction implements Serializable {

    private long id;
    private Date timestamp;
    private User actor;
    private String actionDetails;


    public UserAction() {
    }


    public UserAction(User actor, String actionDetails) {
        this.id = System.currentTimeMillis();
        this.timestamp = new Date();
        this.actor = actor;
        this.actionDetails = actionDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public String getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }

    @Override
    public String toString() {
        return timestamp + " | " + actor.getFirstName() + " | " + actionDetails;
    }

}