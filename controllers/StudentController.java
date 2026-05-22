package controllers;

import core.DataStore;
import models.Course;
import models.Student;
import models.Mark;
import models.Teacher;
import models.StudentOrganization;
import exceptions.CreditLimitException;
import exceptions.MaxFailedReachedException;

import java.util.List;
import java.util.Map;

public class StudentController {

    private final DataStore ds = DataStore.getInstance();

    public StudentController() {
    }

    public List<Course> viewCourses() {
        return ds.getCourses();
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
        ds.log(student, "Registered for course: " + course.getName());
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
