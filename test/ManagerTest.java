package test;

import controllers.ManagerController;
import models.Course;
import models.Teacher;
import models.Student;
import models.User;
import enums.CourseType;
import core.DataStore;

public class ManagerTest {
    public static void test() {
        System.out.println("\n=== ManagerTest ===");

        ManagerController controller = new ManagerController();
        DataStore ds = DataStore.getInstance();

        boolean added = controller.addCourse("Test Course", "TEST101", 3, 1, CourseType.MAJOR);
        assert added : "FAIL: addCourse";
        System.out.println("PASS: addCourse works");

        Course course = new Course(999L, "CS999", "Test", 3, CourseType.MAJOR, 1);
        Teacher teacher = new Teacher(998L, "Prof", "Test", "proftest@test.kz", "pass", 300000);
        boolean assigned = controller.assignCourse(course, teacher);
        assert assigned : "FAIL: assignCourse";
        assert teacher.getActiveCourses().contains(course) : "FAIL: Course not in teacher's active courses";
        System.out.println("PASS: assignCourse works");

        Student student = new Student(997L, "Student", "Test", "studenttest@test.kz", "pass", "CS", 1);
        boolean approved = controller.approveRegistration(student, course);
        assert approved : "FAIL: approveRegistration";
        assert course.getEnrolledStudents().contains(student) : "FAIL: Student not enrolled";
        System.out.println("PASS: approveRegistration works");

        String report = controller.createAcademicReport();
        assert report.contains("ACADEMIC REPORT") : "FAIL: createAcademicReport";
        System.out.println("PASS: createAcademicReport works");
    }
}
