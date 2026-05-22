package controllers;

import core.DataStore;
import models.*;
import exceptions.*;

import java.util.*;

/**
 * Student controller
 */
public class StudentController {

    public StudentController() {
    }

    public List<Course> viewCourses() {
        return DataStore.getInstance().getCourses();
    }

    public boolean registerForCourse(Student student, Course course) throws CreditLimitException, MaxFailedReachedException {
        if (student.getCurrentCredits() + course.getCredits() > 21) {
            throw new CreditLimitException("Credit limit exceeded! Max 21 credits.");
        }
        if (student.getFailCount() >= 3) {
            throw new MaxFailedReachedException("Maximum failed courses (3) reached!");
        }
        student.setCurrentCredits(student.getCurrentCredits() + course.getCredits());
        course.addStudent(student);
        DataStore.getInstance().log(student, "Registered for course: " + course.getName());
        return true;
    }

    public Map<Course, Mark> viewTranscript(Student student) {
        return student.getTranscript();
    }

    public Mark viewMarks(Student student, Course course) {
        return student.getTranscript().get(course);
    }

    public boolean rateTeacher(Teacher teacher, int rating) {
        if (rating < 1 || rating > 5) return false;
        teacher.addRating(rating);
        return true;
    }

    public boolean joinOrganization(Student student, String orgName, boolean isHead) {
        StudentOrganization org = new StudentOrganization(orgName);
        if (isHead) {
            org.setHead(student);
        } else {
            org.addMember(student);
        }
        return true;
    }

}
