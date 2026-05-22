package controllers;

import core.DataStore;
import models.User;
import models.UserAction;
import enums.UserType;
import utils.UserFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AdminController {

    private final DataStore ds = DataStore.getInstance();

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
        return ds.getLogs().stream()
            .map(UserAction::toString)
            .collect(Collectors.toList());
    }

    public boolean setPassword(String email, String newPassword) {
        User u = ds.findUserByEmail(email);
        if (u == null || newPassword == null || newPassword.isBlank()) return false;
        u.setPassword(newPassword);
        ds.log(u, "Password changed by admin");
        return true;
    }
}
