package test;

import models.Course;
import models.Student;
import models.Teacher;
import enums.CourseType;

public class CourseTest {
    public static void test() {
        System.out.println("\n=== CourseTest ===");

        Course course = new Course(1L, "CS101", "Intro to CS", 3, CourseType.MAJOR, 1);
        Student s1 = new Student(100L, "Alice", "Test", "alice@test.kz", "pass", "CS", 1);
        Student s2 = new Student(101L, "Bob", "Test", "bob@test.kz", "pass", "CS", 1);

        course.addStudent(s1);
        course.addStudent(s2);
        assert course.getEnrolledStudents().size() == 2 : "FAIL: addStudent";
        System.out.println("PASS: addStudent works");

        course.removeStudent(s1);
        assert course.getEnrolledStudents().size() == 1 : "FAIL: removeStudent";
        System.out.println("PASS: removeStudent works");

        Teacher teacher = new Teacher(200L, "Prof", "Test", "prof@test.kz", "pass", 300000);
        course.addLectureInstructor(teacher);
        assert course.getLectureInstructors().size() == 1 : "FAIL: addLectureInstructor";
        System.out.println("PASS: addLectureInstructor works");
    }
}
