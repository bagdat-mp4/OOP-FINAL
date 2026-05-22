package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Student extends User {

    private int yearOfStudy;
    private String major;
    private int currentCredits;
    private int failCount;
    private Map<Course, Mark> transcript;
    private List<StudentOrganization> organizations;


    public Student() {
        super();
        this.currentCredits = 0;
        this.failCount = 0;
        this.transcript = new HashMap<>();
        this.organizations = new ArrayList<>();
    }

    public Student(long id, String firstName, String lastName, String email, String password, String major, int yearOfStudy) {
        super(id, firstName, lastName, email, password);
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.currentCredits = 0;
        this.failCount = 0;
        this.transcript = new HashMap<>();
        this.organizations = new ArrayList<>();
    }

    public double getGPA() {
        if (transcript.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Mark mark : transcript.values()) {
            double total = mark.getTotal();
            if (total >= 90) sum += 4.0;
            else if (total >= 80) sum += 3.0;
            else if (total >= 70) sum += 2.0;
            else if (total >= 60) sum += 1.0;
        }
        return sum / transcript.size();
    }

    public Map<Course, Mark> getTranscript() { return transcript; }
    public void setTranscript(Map<Course, Mark> transcript) { this.transcript = transcript; }
    public int getCurrentCredits() { return currentCredits; }
    public void setCurrentCredits(int currentCredits) { this.currentCredits = currentCredits; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }
    public List<StudentOrganization> getOrganizations() { return organizations; }
    public void setOrganizations(List<StudentOrganization> organizations) { this.organizations = organizations; }

    @Override
    public String toString() {
        return "Student: " + getFirstName() + " " + getLastName() + " | Major: " + major + " | GPA: " + String.format("%.2f", getGPA());
    }
}
