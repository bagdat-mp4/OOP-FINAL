package controllers;

import core.DataStore;
import models.Employee;
import models.Manager;
import models.Message;
import models.User;
import models.Course;
import models.Teacher;
import models.Student;
import models.News;
import enums.CourseType;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerController {

    private final DataStore ds = DataStore.getInstance();

    public boolean addCourse(String name, String code, int credits, int targetYear, CourseType type) {
        long id = System.currentTimeMillis();
        ds.addCourse(new Course(id, code, name, credits, type, targetYear));
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
        ds.addNews(new News(title, topic, content));
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
        List<Student> students = ds.getUsers().stream()
            .filter(u -> u instanceof Student)
            .map(u -> (Student) u)
            .collect(Collectors.toList());
        if (criteria.equals("gpa")) {
            students.sort((a, b) -> Double.compare(b.getGPA(), a.getGPA()));
        } else {
            students.sort(Comparator.comparing(User::getLastName));
        }
        return students;
    }

    public boolean makeResearcher(User user) {
        ds.makeResearcher(user);
        ds.removeResearcherRequest(user);
        return true;
    }

    public List<User> getResearcherRequests() {
        return ds.getResearcherRequests();
    }

    public void acceptResearcherRequest(User user) {
        ds.makeResearcher(user);
        ds.removeResearcherRequest(user);
        ds.log(user, "Researcher status granted by manager");
    }

    public void rejectResearcherRequest(User user) {
        ds.removeResearcherRequest(user);
        ds.log(user, "Researcher request rejected by manager");
    }

    public void broadcastMessage(Manager sender, String text) {
        for (User u : ds.getUsers()) {
            if (u instanceof Employee && !u.equals(sender)) {
                Message msg = new Message(sender, (Employee) u, "[BROADCAST] " + text);
                ((Employee) u).getInbox().add(msg);
                ds.addMessage(msg);
            }
        }
        ds.log(sender, "Broadcast sent: " + text);
    }

    public List<Message> getInbox(Manager manager) {
        return manager.getInbox();
    }

    public List<Message> getComplaints(Manager manager) {
        return manager.getInbox().stream()
            .filter(m -> m.getText().startsWith("[COMPLAINT]"))
            .collect(Collectors.toList());
    }
}
