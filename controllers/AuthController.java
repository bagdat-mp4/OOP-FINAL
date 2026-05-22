package controllers;

import core.DataStore;
import models.User;

public class AuthController {

    private final DataStore ds = DataStore.getInstance();

    public AuthController() {
    }

    public User login(String email, String password) {
        User user = ds.findUserByEmail(email);
        if (user != null && user.login(email, password)) {
            ds.log(user, "Logged in");
            return user;
        }
        return null;
    }

}
