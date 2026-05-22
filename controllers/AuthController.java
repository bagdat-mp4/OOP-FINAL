package controllers;

import core.DataStore;
import exceptions.InvalidCredentialsException;
import models.User;

public class AuthController {

    private final DataStore ds = DataStore.getInstance();

    public User login(String email, String password) throws InvalidCredentialsException {
        User user = ds.findUserByEmail(email);
        if (user == null || !user.login(email, password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        ds.log(user, "Logged in");
        return user;
    }
}
