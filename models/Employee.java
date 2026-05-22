package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class Employee extends User {

    private double salary;
    private Date hireDate;
    private List<Message> inbox;


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

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    public List<Message> getInbox() { return inbox; }
    public void setInbox(List<Message> inbox) { this.inbox = inbox; }
}
