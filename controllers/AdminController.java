package controllers;

import core.DataStore;
import models.*;
import enums.*;
import utils.UserFactory;

import java.util.*;

/**
 * Admin controller
 */
public class AdminController {

    public AdminController() {
    }

    public boolean createUser(String email, String firstName, String lastName, String role) {
        DataStore ds = DataStore.getInstance();
        long id = System.currentTimeMillis();
        UserType type = UserType.valueOf(role.toUpperCase());
        User u = UserFactory.createUser(type, id, firstName, lastName, email, "password123");
        if (u == null) return false;
        ds.addUser(u);
        ds.log(u, "User created by admin");
        return true;
    }

    public boolean removeUser(String email) {
        DataStore ds = DataStore.getInstance();
        User u = ds.findUserByEmail(email);
        if (u == null) return false;
        ds.removeUser(u);
        return true;
    }

    public List<User> getAllUsers() {
        return DataStore.getInstance().getUsers();
    }

    public List<String> getLogFiles() {
        List<String> result = new ArrayList<>();
        for (UserAction a : DataStore.getInstance().getLogs()) {
            result.add(a.toString());
        }
        return result;
    }

}
