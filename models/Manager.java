package models;

import enums.ManagerType;

import java.io.*;
import java.util.*;


public class Manager extends Employee {

    private ManagerType type;

    
    public Manager() {
        super();
    }

    
    public Manager(long id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password, salary);
    }

    public ManagerType getManagerType() {
        return type;
    }

    public void setManagerType(ManagerType managerType) {
        this.type = managerType;
    }

    @Override
    public String toString() {
        return "Manager: " + getFirstName() + " " + getLastName() + " (" + type + ")";
    }

}