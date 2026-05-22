package test;

import controllers.TeacherController;
import models.Course;
import models.Student;
import models.Attendance;
import enums.CourseType;
import core.DataStore;

import java.util.Date;

public class AttendanceTest {
    public static void test() {
        System.out.println("\n=== AttendanceTest ===");

        TeacherController controller = new TeacherController();
        DataStore ds = DataStore.getInstance();

        Course course = new Course(801L, "CS801", "Test Course", 3, CourseType.MAJOR, 1);
        Student s1 = new Student(802L, "Alice", "Test", "alice801@test.kz", "pass", "CS", 1);
        Student s2 = new Student(803L, "Bob", "Test", "bob801@test.kz", "pass", "CS", 1);

        controller.markAttendance(course, s1, true);
        controller.markAttendance(course, s1, true);
        controller.markAttendance(course, s1, false);
        controller.markAttendance(course, s2, true);

        String report = controller.getAttendanceReport(course);
        assert report.contains("Alice Test") : "FAIL: Report should contain Alice";
        assert report.contains("66.7%") || report.contains("66.6%") : "FAIL: Percentage calculation wrong";
        System.out.println("PASS: markAttendance works");
        System.out.println("PASS: getAttendanceReport percentage calculation correct");
    }
}
