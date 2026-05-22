package controllers;

import core.DataStore;
import models.Course;
import models.Teacher;
import models.Student;
import models.Mark;
import models.Attendance;
import enums.UrgencyLevel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherController {

    private final DataStore ds = DataStore.getInstance();

    public List<Course> viewCourses(Teacher teacher) {
        return teacher.getActiveCourses();
    }

    public List<Student> viewStudents(Course course) {
        return course.getEnrolledStudents();
    }

    public boolean putMark(Student student, Course course, String type, double score) {
        if (!course.getEnrolledStudents().contains(student)) return false;
        Mark mark = student.getTranscript().getOrDefault(course, new Mark());
        if (!isValidScore(type, score, mark)) return false;
        switch (type) {
            case "AT1":   mark.setFirstAttestation(score); break;
            case "AT2":   mark.setSecondAttestation(score); break;
            case "FINAL":
                mark.setFinalExam(score);
                if (mark.getTotal() < 50) {
                    student.setFailCount(student.getFailCount() + 1);
                }
                break;
            default: return false;
        }
        student.getTranscript().put(course, mark);
        ds.log(student, "Mark updated for course: " + course.getName());
        return true;
    }

    private boolean isValidScore(String type, double score, Mark mark) {
        if (score < 0) return false;
        switch (type) {
            case "AT1":
            case "AT2":   return score <= 30;
            case "FINAL":
                if (score > 40) return false;
                double atTotal = mark.getFirstAttestation() + mark.getSecondAttestation();
                return atTotal >= 30;
            default:      return false;
        }
    }

    public String getMarkError(Student student, Course course, String type, double score) {
        if (!course.getEnrolledStudents().contains(student))
            return "Student is not enrolled in this course.";
        if (score < 0) return "Score cannot be negative.";
        switch (type) {
            case "AT1":
            case "AT2":
                if (score > 30) return type + " cannot exceed 30.";
                return "";
            case "FINAL":
                if (score > 40) return "Final cannot exceed 40.";
                Mark mark = student.getTranscript().getOrDefault(course, new Mark());
                double atTotal = mark.getFirstAttestation() + mark.getSecondAttestation();
                if (atTotal < 30) return "Not admitted to Final: AT1+AT2 = " +
                    String.format("%.1f", atTotal) + " (minimum 30 required).";
                return "";
            default: return "Unknown mark type.";
        }
    }

    public String getScoreRange(String type) {
        switch (type) {
            case "AT1":
            case "AT2":   return "0 - 30";
            case "FINAL": return "0 - 40";
            default:      return "";
        }
    }

    public boolean sendComplaint(Teacher teacher, Student student, String text, UrgencyLevel urgency) {
        String body = "[COMPLAINT][" + urgency + "] About: " +
            student.getFirstName() + " " + student.getLastName() + " — " + text;

        for (models.User u : ds.getUsers()) {
            if (u instanceof models.Manager) {
                models.Message msg = new models.Message(teacher, (models.Manager) u, body);
                ((models.Manager) u).getInbox().add(msg);
                ds.addMessage(msg);
            }
        }
        ds.log(teacher, "Complaint sent about " + student.getFirstName() + ": " + urgency);
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
                      .append(s.getLastName()).append(": ").append(m).append("\n");
                    totalGpa += m.getTotal();
                    count++;
                }
            }
            if (count > 0) {
                sb.append("  Average: ").append(String.format("%.2f", totalGpa / count)).append("\n");
            }
        }
        return sb.toString();
    }

    public void markAttendance(Course course, Student student, boolean present) {
        ds.addAttendance(new Attendance(student, course, new Date(), present));
        ds.log(student, "Attendance: " + (present ? "PRESENT" : "ABSENT") + " in " + course.getName());
    }

    public String getAttendanceReport(Course course) {
        StringBuilder sb = new StringBuilder("=== ATTENDANCE REPORT: " + course.getName() + " ===\n");
        Map<String, int[]> stats = new HashMap<>();

        for (Attendance att : ds.getAttendances()) {
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
            double pct = total > 0 ? (double) present / total * 100 : 0;
            sb.append(String.format("%-25s %d/%d (%.1f%%)%n", e.getKey(), present, total, pct));
        }
        return sb.toString();
    }
}
