package models;

import java.io.*;
import java.util.*;


public class Admin extends Employee {

    
    public Admin() {
        super();
    }

    
    public Admin(long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password, 0.0);
    }

    @Override
    public String toString() {
        return "Admin: " + getFirstName() + " " + getLastName();
    }

}