package models;

import models.User;

import java.io.*;
import java.util.*;


public abstract class Employee extends User {

    private double salary;
    private Date hireDate;
    private List<models.Message> inbox;


    public Employee() {
        super();
        this.inbox = new ArrayList<>();
    }


    public Employee(long id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password);
        this.salary = salary;
        this.hireDate = new Date();
        this.inbox = new ArrayList<>();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public List<models.Message> getInbox() {
        return inbox;
    }

    public void setInbox(List<models.Message> inbox) {
        this.inbox = inbox;
    }

}