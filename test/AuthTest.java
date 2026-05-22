package test;

import controllers.AuthController;
import core.DataStore;
import exceptions.InvalidCredentialsException;
import models.User;
import models.Admin;

public class AuthTest {
    public static void test() {
        System.out.println("\n=== AuthTest ===");

        DataStore ds = DataStore.getInstance();
        Admin testUser = new Admin(999L, "Test", "User", "test@test.kz", "pass123");
        ds.addUser(testUser);

        AuthController auth = new AuthController();

        try {
            User user1 = auth.login("test@test.kz", "pass123");
            assert user1 != null : "FAIL: Valid login returned null";
            System.out.println("PASS: Valid login");
        } catch (InvalidCredentialsException e) {
            System.out.println("FAIL: Valid login threw exception: " + e.getMessage());
        }

        try {
            auth.login("test@test.kz", "wrongpassword");
            System.out.println("FAIL: Invalid password should throw exception");
        } catch (InvalidCredentialsException e) {
            System.out.println("PASS: Invalid password throws InvalidCredentialsException");
        }

        try {
            auth.login("notexist@test.kz", "anypass");
            System.out.println("FAIL: Non-existent email should throw exception");
        } catch (InvalidCredentialsException e) {
            System.out.println("PASS: Non-existent email throws InvalidCredentialsException");
        }

        ds.removeUser(testUser);
    }
}
