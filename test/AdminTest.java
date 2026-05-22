package test;

import controllers.AdminController;
import models.User;
import core.DataStore;

import java.util.List;

public class AdminTest {
    public static void test() {
        System.out.println("\n=== AdminTest ===");

        AdminController controller = new AdminController();
        DataStore ds = DataStore.getInstance();

        int initialCount = ds.getUsers().size();
        boolean created = controller.createUser("newuser@test.kz", "New", "User", "STUDENT");
        assert created : "FAIL: createUser";
        assert ds.getUsers().size() == initialCount + 1 : "FAIL: User not added";
        System.out.println("PASS: createUser works");

        boolean removed = controller.removeUser("newuser@test.kz");
        assert removed : "FAIL: removeUser";
        assert ds.getUsers().size() == initialCount : "FAIL: User not removed";
        System.out.println("PASS: removeUser works");

        List<User> users = controller.getAllUsers();
        assert users != null : "FAIL: getAllUsers";
        assert users.size() > 0 : "FAIL: getAllUsers should return users";
        System.out.println("PASS: getAllUsers works");

        List<String> logs = controller.getLogFiles();
        assert logs != null : "FAIL: getLogFiles";
        System.out.println("PASS: getLogFiles works");
    }
}
