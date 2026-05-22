package models;

import enums.CourseType;

import java.io.*;
import java.util.*;


public class Course implements Serializable {

    private long id;
    private String courseCode;
    private String name;
    private int credits;
    private CourseType type;
    private int targetYear;
    private List<Teacher> lectureInstructors;
    private List<Teacher> practiceInstructors;
    private List<Student> enrolledStudents;

    
    public Course() {
        this.lectureInstructors = new ArrayList<>();
        this.practiceInstructors = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    
    public Course(long id, String courseCode, String name, int credits, CourseType type, int targetYear) {
        this.id = id;
        this.courseCode = courseCode;
        this.name = name;
        this.credits = credits;
        this.type = type;
        this.targetYear = targetYear;
        this.lectureInstructors = new ArrayList<>();
        this.practiceInstructors = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public void addLectureInstructor(Teacher t) {
        lectureInstructors.add(t);
    }

    public void addPracticeInstructor(Teacher t) {
        practiceInstructors.add(t);
    }

    public void addStudent(Student s) {
        if (!enrolledStudents.contains(s)) {
            enrolledStudents.add(s);
        }
    }

    public void removeStudent(Student s) {
        enrolledStudents.remove(s);
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public int getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }

    public List<Teacher> getLectureInstructors() {
        return lectureInstructors;
    }

    public void setLectureInstructors(List<Teacher> lectureInstructors) {
        this.lectureInstructors = lectureInstructors;
    }

    public List<Teacher> getPracticeInstructors() {
        return practiceInstructors;
    }

    public void setPracticeInstructors(List<Teacher> practiceInstructors) {
        this.practiceInstructors = practiceInstructors;
    }

    @Override
    public String toString() {
        return "[" + courseCode + "] " + name + " (" + credits + " credits, " + type + ")";
    }

}