package test;

import controllers.AuthController;
import core.DataStore;
import models.*;

public class AuthTest {
    public static void main(String[] args) {
        System.out.println("=== Auth Tests ===");

        DataStore ds = DataStore.getInstance();
        AuthController auth = new AuthController();

        // Test 1: Valid login
        User user = auth.login("admin@uni.kz", "admin123");
        assert user != null : "FAIL: Valid login returned null";
        System.out.println("PASS: Valid login - " + user.getFirstName());

        // Test 2: Invalid password
        User user2 = auth.login("admin@uni.kz", "wrongpassword");
        assert user2 == null : "FAIL: Invalid password should return null";
        System.out.println("PASS: Invalid password returns null");

        // Test 3: Non-existent email
        User user3 = auth.login("notexist@uni.kz", "anypass");
        assert user3 == null : "FAIL: Non-existent email should return null";
        System.out.println("PASS: Non-existent email returns null");

        System.out.println("All Auth tests passed!");
    }
}
