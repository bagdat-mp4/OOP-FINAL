package models;

import java.io.Serializable;
import java.util.Date;

public class RecommendationLetter implements Serializable {
    private Teacher author;
    private Student student;
    private String content;
    private Date dateCreated;
    private String purpose;

    public RecommendationLetter(Teacher author, Student student, String content, String purpose) {
        this.author = author;
        this.student = student;
        this.content = content;
        this.purpose = purpose;
        this.dateCreated = new Date();
    }

    public Teacher getAuthor() { return author; }
    public void setAuthor(Teacher author) { this.author = author; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    @Override
    public String toString() {
        return "=== RECOMMENDATION LETTER ===\n" +
               "Date: " + dateCreated + "\n" +
               "From: " + author.getFirstName() + " " + author.getLastName() +
               " (" + author.getTitle() + ")\n" +
               "To Whom It May Concern,\n\n" +
               content + "\n\n" +
               "Purpose: " + purpose + "\n" +
               "Signed: " + author.getFirstName() + " " + author.getLastName() + "\n" +
               "===========================";
    }
}
