package models;

import java.io.Serializable;
import java.util.*;

public class Attendance implements Serializable {
    private Student student;
    private Course course;
    private Date date;
    private boolean present;
    private String note;

    public Attendance(Student student, Course course, Date date, boolean present) {
        this.student = student;
        this.course = course;
        this.date = date;
        this.present = present;
    }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return student.getFirstName() + " | " + course.getCourseCode() +
               " | " + date + " | " + (present ? "PRESENT " : "ABSENT ");
    }
}
