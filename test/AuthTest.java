package test;

import controllers.AuthController;
import core.DataStore;
import models.User;
import models.Admin;

public class AuthTest {
    public static void test() {
        System.out.println("\n=== AuthTest ===");

        DataStore ds = DataStore.getInstance();
        Admin testUser = new Admin(999L, "Test", "User", "test@test.kz", "pass123");
        ds.addUser(testUser);

        AuthController auth = new AuthController();

        User user1 = auth.login("test@test.kz", "pass123");
        assert user1 != null : "FAIL: Valid login returned null";
        System.out.println("PASS: Valid login");

        User user2 = auth.login("test@test.kz", "wrongpassword");
        assert user2 == null : "FAIL: Invalid password should return null";
        System.out.println("PASS: Invalid password returns null");

        User user3 = auth.login("notexist@test.kz", "anypass");
        assert user3 == null : "FAIL: Non-existent email should return null";
        System.out.println("PASS: Non-existent email returns null");

        ds.removeUser(testUser);
    }
}
