package test;

import models.Student;
import models.Course;
import models.Mark;
import controllers.StudentController;
import exceptions.CreditLimitException;
import exceptions.MaxFailedReachedException;
import enums.CourseType;

public class StudentTest {
    public static void test() {
        System.out.println("\n=== StudentTest ===");

        Student student = new Student(100L, "Test", "User", "test@uni.kz", "pass", "CS", 1);
        student.setCurrentCredits(20);
        Course bigCourse = new Course(99L, "BIG001", "Big Course", 5, CourseType.MAJOR, 1);

        StudentController controller = new StudentController();
        boolean threw = false;
        try {
            controller.registerForCourse(student, bigCourse);
        } catch (CreditLimitException e) {
            threw = true;
            System.out.println("PASS: CreditLimitException thrown");
        } catch (MaxFailedReachedException e) {
            System.out.println("FAIL: Wrong exception");
        }
        assert threw : "FAIL: CreditLimitException not thrown";

        Student student2 = new Student(101L, "GPA", "Test", "gpa@uni.kz", "pass", "CS", 1);
        Course c1 = new Course(1L, "CS101", "Course1", 3, CourseType.MAJOR, 1);
        Mark m1 = new Mark();
        m1.setFirstAttestation(95);
        m1.setSecondAttestation(95);
        m1.setFinalExam(95);
        student2.getTranscript().put(c1, m1);
        assert student2.getGPA() == 4.0 : "FAIL: GPA should be 4.0, got " + student2.getGPA();
        System.out.println("PASS: GPA = 4.0");

        Student student3 = new Student(102L, "Fail", "Test", "fail@uni.kz", "pass", "CS", 1);
        student3.setFailCount(3);
        Course c2 = new Course(2L, "CS102", "Course2", 3, CourseType.MAJOR, 1);
        boolean threwFail = false;
        try {
            controller.registerForCourse(student3, c2);
        } catch (MaxFailedReachedException e) {
            threwFail = true;
            System.out.println("PASS: MaxFailedReachedException thrown");
        } catch (CreditLimitException e) {
            System.out.println("FAIL: Wrong exception");
        }
        assert threwFail : "FAIL: MaxFailedReachedException not thrown";
    }
}
