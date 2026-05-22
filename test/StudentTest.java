package test;

import models.*;
import controllers.StudentController;
import exceptions.*;
import enums.*;

public class StudentTest {
    public static void main(String[] args) {
        System.out.println("=== Student Tests ===");

        // Test 1: Credit limit check
        Student student = new Student(100L, "Test", "User", "test@uni.kz", "pass", "CS", 1);
        student.setCurrentCredits(20);
        Course bigCourse = new Course(99L, "BIG001", "Big Course", 5, CourseType.MAJOR, 1);

        StudentController controller = new StudentController();
        boolean threw = false;
        try {
            controller.registerForCourse(student, bigCourse);
        } catch (CreditLimitException e) {
            threw = true;
            System.out.println("PASS: CreditLimitException thrown - " + e.getMessage());
        } catch (MaxFailedReachedException e) {
            System.out.println("FAIL: Wrong exception");
        }
        assert threw : "FAIL: CreditLimitException not thrown";

        // Test 2: GPA calculation (4.0 scale)
        Student student2 = new Student(101L, "GPA", "Test", "gpa@uni.kz", "pass", "CS", 1);
        Course c1 = new Course(1L, "CS101", "Course1", 3, CourseType.MAJOR, 1);
        Mark m1 = new Mark();
        m1.setFirstAttestation(95);
        m1.setSecondAttestation(95);
        m1.setFinalExam(95); // total = 95, GPA point = 4.0
        student2.getTranscript().put(c1, m1);
        assert student2.getGPA() == 4.0 : "FAIL: GPA should be 4.0, got " + student2.getGPA();
        System.out.println("PASS: GPA = " + student2.getGPA() + " (expected 4.0)");

        // Test 3: Fail count check
        Student student3 = new Student(102L, "Fail", "Test", "fail@uni.kz", "pass", "CS", 1);
        student3.setFailCount(3);
        Course c2 = new Course(2L, "CS102", "Course2", 3, CourseType.MAJOR, 1);
        boolean threwFail = false;
        try {
            controller.registerForCourse(student3, c2);
        } catch (MaxFailedReachedException e) {
            threwFail = true;
            System.out.println("PASS: MaxFailedReachedException - " + e.getMessage());
        } catch (CreditLimitException e) {
            System.out.println("FAIL: Wrong exception");
        }
        assert threwFail : "FAIL: MaxFailedReachedException not thrown";

        System.out.println("All Student tests passed!");
    }
}
