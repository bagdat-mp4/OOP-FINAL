package controllers;

import core.DataStore;
import models.User;
import models.Course;
import models.Teacher;
import models.Student;
import models.News;
import enums.CourseType;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class ManagerController {

    private final DataStore ds = DataStore.getInstance();

    public ManagerController() {
    }

    public boolean addCourse(String name, String code, int credits, int targetYear, CourseType type) {
        long id = System.currentTimeMillis();
        Course c = new Course(id, code, name, credits, type, targetYear);
        ds.addCourse(c);
        return true;
    }

    public boolean assignCourse(Course course, Teacher teacher) {
        course.addLectureInstructor(teacher);
        teacher.getActiveCourses().add(course);
        return true;
    }

    public boolean approveRegistration(Student student, Course course) {
        if (!course.getEnrolledStudents().contains(student)) {
            course.addStudent(student);
        }
        return true;
    }

    public void addNews(String title, String content, boolean isResearch) {
        String topic = isResearch ? "Research" : "General";
        News n = new News(title, topic, content);
        ds.addNews(n);
    }

    public String createAcademicReport() {
        StringBuilder sb = new StringBuilder("=== ACADEMIC REPORT ===\n");
        for (User u : ds.getUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                sb.append(s.getFirstName()).append(" ").append(s.getLastName())
                  .append(" | GPA: ").append(String.format("%.2f", s.getGPA()))
                  .append(" | Credits: ").append(s.getCurrentCredits()).append("\n");
            }
        }
        return sb.toString();
    }

    public List<Student> viewStudentsSorted(String criteria) {
        List<Student> students = new ArrayList<>();
        for (User u : ds.getUsers()) {
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
        ds.makeResearcher(user);
        return true;
    }

}
