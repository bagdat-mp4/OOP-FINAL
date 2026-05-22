package models;

import enums.RequestStatus;

import java.io.*;
import java.util.*;

/**
 * Tech support request class
 */
public class TechSupportRequest implements Serializable {

    private models.User sender;
    private String issue;
    private RequestStatus status;

    /**
     * Default constructor
     */
    public TechSupportRequest() {
        this.status = RequestStatus.NEW;
    }

    /**
     * Constructor with parameters
     */
    public TechSupportRequest(models.User sender, String issue) {
        this.sender = sender;
        this.issue = issue;
        this.status = RequestStatus.NEW;
    }

    public models.User getSender() {
        return sender;
    }

    public void setSender(models.User sender) {
        this.sender = sender;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[" + status + "] " + issue + " (from: " + sender.getFirstName() + ")";
    }

}