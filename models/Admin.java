package models;

import java.io.*;
import java.util.*;

/**
 * Admin user with system management privileges
 */
public class Admin extends Employee {

    /**
     * Default constructor
     */
    public Admin() {
        super();
    }

    /**
     * Constructor with parameters
     */
    public Admin(long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password, 0.0);
    }

    @Override
    public String toString() {
        return "Admin: " + getFirstName() + " " + getLastName();
    }

}