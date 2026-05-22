package controllers;

import core.DataStore;
import models.User;
import models.UserAction;
import enums.UserType;
import utils.UserFactory;

import java.util.List;
import java.util.ArrayList;

public class AdminController {

    private final DataStore ds = DataStore.getInstance();

    public AdminController() {
    }

    public boolean createUser(String email, String firstName, String lastName, String role) {
        long id = System.currentTimeMillis();
        UserType type = UserType.valueOf(role.toUpperCase());
        User u = UserFactory.createUser(type, id, firstName, lastName, email, "password123");
        if (u == null) return false;
        ds.addUser(u);
        ds.log(u, "User created by admin");
        return true;
    }

    public boolean removeUser(String email) {
        User u = ds.findUserByEmail(email);
        if (u == null) return false;
        ds.removeUser(u);
        return true;
    }

    public List<User> getAllUsers() {
        return ds.getUsers();
    }

    public List<String> getLogFiles() {
        List<String> result = new ArrayList<>();
        for (UserAction a : ds.getLogs()) {
            result.add(a.toString());
        }
        return result;
    }

}
