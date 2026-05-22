package models;

import java.io.*;
import java.util.*;

/**
 * Student class representing undergraduate students
 */
public class Student extends User implements Serializable {

    private int yearOfStudy;
    private String major;
    private int currentCredits;
    private int failCount;
    private Map<Course, Mark> transcript;
    private List<StudentOrganization> organizations;

    /**
     * Default constructor
     */
    public Student() {
        super();
        this.currentCredits = 0;
        this.failCount = 0;
        this.transcript = new HashMap<>();
        this.organizations = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public Student(long id, String firstName, String lastName, String email, String password, String major, int yearOfStudy) {
        super(id, firstName, lastName, email, password);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.currentCredits = 0;
        this.failCount = 0;
        this.transcript = new HashMap<>();
        this.organizations = new ArrayList<>();
    }

    public Map<Course, Mark> getTranscript() {
        return transcript;
    }

    public void setTranscript(Map<Course, Mark> transcript) {
        this.transcript = transcript;
    }

    public int getCurrentCredits() {
        return currentCredits;
    }

    public void setCurrentCredits(int currentCredits) {
        this.currentCredits = currentCredits;
    }

    public double getGPA() {
        if (transcript.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Mark mark : transcript.values()) {
            double total = mark.getTotal();
            double gpaPoint;
            if (total >= 90) gpaPoint = 4.0;
            else if (total >= 80) gpaPoint = 3.0;
            else if (total >= 70) gpaPoint = 2.0;
            else if (total >= 60) gpaPoint = 1.0;
            else gpaPoint = 0.0;
            sum += gpaPoint;
        }
        return sum / transcript.size();
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public int getFailCount() {
        return failCount;
    }

    public List<StudentOrganization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<StudentOrganization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public String toString() {
        return "Student: " + getFirstName() + " " + getLastName() + " | Major: " + major + " | GPA: " + String.format("%.2f", getGPA());
    }

}