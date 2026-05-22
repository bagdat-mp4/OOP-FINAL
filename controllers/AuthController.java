package controllers;

import core.DataStore;
import models.User;

/**
 * Authentication controller
 */
public class AuthController {

    public AuthController() {
    }

    public User login(String email, String password) {
        DataStore ds = DataStore.getInstance();
        User user = ds.findUserByEmail(email);
        if (user != null && user.login(email, password)) {
            ds.log(user, "Logged in");
            return user;
        }
        return null;
    }

    public boolean logout(String userId) {
        return true;
    }

}
