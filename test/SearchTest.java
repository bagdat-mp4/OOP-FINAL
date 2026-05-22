package test;

import controllers.SearchController;
import models.User;
import models.Course;
import models.ResearchPaper;
import models.Student;
import enums.CourseType;
import core.DataStore;

import java.util.List;

public class SearchTest {
    public static void test() {
        System.out.println("\n=== SearchTest ===");

        SearchController controller = new SearchController();
        DataStore ds = DataStore.getInstance();

        Student testStudent = new Student(601L, "SearchTest", "User", "searchtest@test.kz", "pass", "CS", 1);
        ds.addUser(testStudent);

        List<User> users = controller.searchUsersByRegex("SearchTest");
        assert users.size() > 0 : "FAIL: searchUsersByRegex should find user";
        assert users.get(0).getFirstName().equals("SearchTest") : "FAIL: Wrong user found";
        System.out.println("PASS: searchUsersByRegex works");

        Course testCourse = new Course(602L, "SEARCH101", "Search Course", 3, CourseType.MAJOR, 1);
        ds.addCourse(testCourse);

        List<Course> courses = controller.searchCoursesByRegex("SEARCH101");
        assert courses.size() > 0 : "FAIL: searchCoursesByRegex should find course";
        System.out.println("PASS: searchCoursesByRegex works");

        List<User> invalidUsers = controller.searchUsersByRegex("[invalid(regex");
        assert invalidUsers.size() == 0 : "FAIL: Invalid regex should return empty list";
        System.out.println("PASS: Invalid regex handling works");

        ds.removeUser(testStudent);
        ds.getCourses().remove(testCourse);
    }
}
