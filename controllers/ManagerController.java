package controllers;

import core.DataStore;
import models.*;
import enums.*;

import java.util.*;

/**
 * Manager controller
 */
public class ManagerController {

    public ManagerController() {
    }

    public boolean addCourse(String name, String code, int credits, int targetYear, enums.CourseType type) {
        long id = System.currentTimeMillis();
        models.Course c = new models.Course(id, code, name, credits, type, targetYear);
        DataStore.getInstance().addCourse(c);
        return true;
    }

    public boolean assignCourse(models.Course course, models.Teacher teacher) {
        course.addLectureInstructor(teacher);
        teacher.getActiveCourses().add(course);
        return true;
    }

    public boolean approveRegistration(models.Student student, models.Course course) {
        if (!course.getEnrolledStudents().contains(student)) {
            course.addStudent(student);
        }
        return true;
    }

    public void addNews(String title, String content, boolean isResearch) {
        String topic = isResearch ? "Research" : "General";
        models.News n = new models.News(title, topic, content);
        DataStore.getInstance().addNews(n);
    }

    public String createAcademicReport() {
        StringBuilder sb = new StringBuilder("=== ACADEMIC REPORT ===\n");
        for (models.User u : DataStore.getInstance().getUsers()) {
            if (u instanceof models.Student) {
                models.Student s = (models.Student) u;
                sb.append(s.getFirstName()).append(" ").append(s.getLastName())
                  .append(" | GPA: ").append(String.format("%.2f", s.getGPA()))
                  .append(" | Credits: ").append(s.getCurrentCredits()).append("\n");
            }
        }
        return sb.toString();
    }

    public List<Student> viewStudentsSorted(String criteria) {
        List<Student> students = new ArrayList<>();
        for (User u : DataStore.getInstance().getUsers()) {
            if (u instanceof Student) students.add((Student) u);
        }
        if (criteria.equals("gpa")) {
            students.sort((a, b) -> Double.compare(b.getGPA(), a.getGPA()));
        } else {
            students.sort(Comparator.comparing(User::getLastName));
        }
        return students;
    }

    public boolean makeResearcher(User user) {
        DataStore.getInstance().makeResearcher(user);
        return true;
    }

}
