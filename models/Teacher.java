package models;

import enums.TeacherTitle;
import java.util.ArrayList;
import java.util.List;


public class Teacher extends Employee {

    private TeacherTitle title;
    private List<Course> activeCourses;
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
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public void addRating(double r) {
        ratings.add(r);
    }

    public TeacherTitle getTitle() { return title; }
    public void setTitle(TeacherTitle title) { this.title = title; }
    public List<Course> getActiveCourses() { return activeCourses; }
    public void setActiveCourses(List<Course> activeCourses) { this.activeCourses = activeCourses; }
    public List<Double> getRatings() { return ratings; }
    public void setRatings(List<Double> ratings) { this.ratings = ratings; }

    @Override
    public String toString() {
        return "Teacher: " + getFirstName() + " " + getLastName() + " (" + title + ")";
    }
}
