package models;

import core.DataStore;
import enums.RequestStatus;

import java.io.*;
import java.util.*;

/**
 * Tech support specialist class
 */
public class TechSupportSpecialist extends models.Employee {

    /**
     * Default constructor
     */
    public TechSupportSpecialist() {
        super();
    }

    /**
     * Constructor with parameters
     */
    public TechSupportSpecialist(long id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password, salary);
    }

    public List<models.TechSupportRequest> getRequests() {
        return DataStore.getInstance().getTechSupportRequests();
    }

    public void acceptRequest(models.TechSupportRequest r) {
        r.setStatus(RequestStatus.ACCEPTED);
    }

    public void rejectRequest(models.TechSupportRequest r) {
        r.setStatus(RequestStatus.REJECTED);
    }

    public void markDone(models.TechSupportRequest r) {
        r.setStatus(RequestStatus.DONE);
    }

    public void viewRequest(models.TechSupportRequest r) {
        r.setStatus(RequestStatus.VIEWED);
    }

    @Override
    public String toString() {
        return "TechSupport: " + getFirstName() + " " + getLastName();
    }

}