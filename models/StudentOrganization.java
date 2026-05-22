package models;

import java.io.*;
import java.util.*;

/**
 * Student organization class
 */
public class StudentOrganization implements Serializable {

    private String name;
    private models.Student head;
    private List<models.Student> members;

    /**
     * Default constructor
     */
    public StudentOrganization() {
        this.members = new ArrayList<>();
    }

    /**
     * Constructor with name
     */
    public StudentOrganization(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(models.Student student) {
        if (!members.contains(student)) {
            members.add(student);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public models.Student getHead() {
        return head;
    }

    public void setHead(models.Student student) {
        this.head = student;
    }

    public List<models.Student> getMembers() {
        return members;
    }

    public void setMembers(List<models.Student> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Organization: " + name + " | Head: " + (head != null ? head.getFirstName() : "none") + " | Members: " + members.size();
    }

}