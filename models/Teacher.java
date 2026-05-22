package models;

import enums.TeacherTitle;

import java.io.*;
import java.util.*;


public class Teacher extends models.Employee {

    private TeacherTitle title;
    private List<models.Course> activeCourses;
    private List<Double> ratings;


    public Teacher() {
        super();
        this.activeCourses = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }


    public Teacher(long id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password, salary);
        this.activeCourses = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public double getRating() {
        if (ratings.isEmpty()) return 0.0;
        double sum = 0.0;
        for (Double r : ratings) {
            sum += r;
        }
        return sum / ratings.size();
    }

    public void addRating(double r) {
        ratings.add(r);
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public void setTitle(TeacherTitle title) {
        this.title = title;
    }

    public List<models.Course> getActiveCourses() {
        return activeCourses;
    }

    public void setActiveCourses(List<models.Course> activeCourses) {
        this.activeCourses = activeCourses;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Teacher: " + getFirstName() + " " + getLastName() + " (" + title + ")";
    }

}