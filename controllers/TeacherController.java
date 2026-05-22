package controllers;

import core.DataStore;
import models.*;
import enums.UrgencyLevel;

import java.util.*;

/**
 * Teacher controller
 */
public class TeacherController {

    public TeacherController() {
    }

    public List<Course> viewCourses(Teacher teacher) {
        return teacher.getActiveCourses();
    }

    public List<Student> viewStudents(Course course) {
        return course.getEnrolledStudents();
    }

    public boolean putMark(Student student, Course course, String type, double score) {
        Mark mark = student.getTranscript().getOrDefault(course, new Mark());
        if (type.equals("AT1")) {
            mark.setFirstAttestation(score);
        } else if (type.equals("AT2")) {
            mark.setSecondAttestation(score);
        } else if (type.equals("FINAL")) {
            mark.setFinalExam(score);
            if (mark.getTotal() < 50) {
                student.setFailCount(student.getFailCount() + 1);
            }
        }
        student.getTranscript().put(course, mark);
        DataStore.getInstance().log(student, "Mark updated for course: " + course.getName());
        return true;
    }

    public boolean sendComplaint(Student student, String text, UrgencyLevel urgency) {
        System.out.println("[COMPLAINT to Dean] About: " + student.getFirstName() +
            " | Urgency: " + urgency + " | Text: " + text);
        DataStore.getInstance().log(student, "Complaint sent: " + urgency);
        return true;
    }

    public String generateMarkReport(Teacher teacher) {
        StringBuilder sb = new StringBuilder("=== MARK REPORT ===\n");
        for (Course course : teacher.getActiveCourses()) {
            sb.append("\nCourse: ").append(course.getName()).append("\n");
            sb.append("Students: ").append(course.getEnrolledStudents().size()).append("\n");
            double totalGpa = 0;
            int count = 0;
            for (Student s : course.getEnrolledStudents()) {
                Mark m = s.getTranscript().get(course);
                if (m != null) {
                    sb.append("  ").append(s.getFirstName()).append(" ")
                      .append(s.getLastName()).append(": ")
                      .append(m.toString()).append("\n");
                    totalGpa += m.getTotal();
                    count++;
                }
            }
            if (count > 0) sb.append("  Average: ").append(String.format("%.2f", totalGpa/count)).append("\n");
        }
        return sb.toString();
    }

    public void markAttendance(Course course, Student student, boolean present) {
        Attendance att = new Attendance(student, course, new java.util.Date(), present);
        DataStore.getInstance().addAttendance(att);
        DataStore.getInstance().log(student, "Attendance: " + (present ? "PRESENT" : "ABSENT") + " in " + course.getName());
    }

    public String getAttendanceReport(Course course) {
        StringBuilder sb = new StringBuilder("=== ATTENDANCE REPORT: " + course.getName() + " ===\n");
        Map<String, int[]> stats = new HashMap<>();

        for (Attendance att : DataStore.getInstance().getAttendances()) {
            if (att.getCourse().equals(course)) {
                String name = att.getStudent().getFirstName() + " " + att.getStudent().getLastName();
                stats.putIfAbsent(name, new int[]{0, 0});
                stats.get(name)[1]++;
                if (att.isPresent()) stats.get(name)[0]++;
            }
        }

        for (Map.Entry<String, int[]> e : stats.entrySet()) {
            int present = e.getValue()[0];
            int total = e.getValue()[1];
            double pct = total > 0 ? (double)present/total*100 : 0;
            sb.append(String.format("%-25s %d/%d (%.1f%%)%n", e.getKey(), present, total, pct));
        }
        return sb.toString();
    }

}
