package test;

import controllers.TeacherController;
import models.Teacher;
import models.Student;
import models.Course;
import models.Mark;
import enums.CourseType;
import enums.UrgencyLevel;

public class TeacherTest {
    public static void test() {
        System.out.println("\n=== TeacherTest ===");

        TeacherController controller = new TeacherController();
        Teacher teacher = new Teacher(200L, "Prof", "Test", "prof@test.kz", "pass", 300000);
        Student student = new Student(100L, "Alice", "Test", "alice@test.kz", "pass", "CS", 1);
        Course course = new Course(1L, "CS101", "Intro", 3, CourseType.MAJOR, 1);

        controller.putMark(student, course, "AT1", 25);
        controller.putMark(student, course, "AT2", 25);
        controller.putMark(student, course, "FINAL", 35);
        Mark m = student.getTranscript().get(course);
        assert m.getFirstAttestation() == 25 : "FAIL: AT1 mark";
        assert m.getSecondAttestation() == 25 : "FAIL: AT2 mark";
        assert m.getFinalExam() == 35 : "FAIL: FINAL mark";
        System.out.println("PASS: putMark AT1/AT2/FINAL works");

        teacher.addRating(5.0);
        teacher.addRating(4.0);
        double rating = teacher.getRating();
        assert rating == 4.5 : "FAIL: getRating should be 4.5, got " + rating;
        System.out.println("PASS: getRating works");

        boolean sent = controller.sendComplaint(teacher, student, "Test complaint", UrgencyLevel.HIGH);
        assert sent : "FAIL: sendComplaint";
        System.out.println("PASS: sendComplaint works");
    }
}
