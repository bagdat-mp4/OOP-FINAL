package views;

import controllers.*;
import models.*;
import enums.*;
import core.DataStore;
import exceptions.*;


public class StudentView extends BaseView {

    protected Student student;
    protected StudentController controller = new StudentController();

    public StudentView(Student student) {
        this.student = student;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== STUDENT MENU === [" + student.getFirstName() + "]");
            System.out.println("1. View available courses");
            System.out.println("2. Register for course");
            System.out.println("3. View my transcript");
            System.out.println("4. View news");
            System.out.println("5. Change language");
            System.out.println("6. View my schedule");
            System.out.println("7. View my recommendation letters");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1: showCourseRegisterForm(); break;
                case 2: registerCourse(); break;
                case 3: showTranscript(); break;
                case 4: showNews(); break;
                case 5: changeLanguageMenu(); break;
                case 6: viewSchedule(); break;
                case 7: viewRecommendationLetters(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    public void showCourseRegisterForm() {
        System.out.println("\n=== AVAILABLE COURSES ===");
        java.util.List<Course> courses = controller.viewCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
    }

    public void registerCourse() {
        showCourseRegisterForm();
        System.out.print("Enter course number: ");
        int idx = readInt() - 1;
        java.util.List<Course> courses = controller.viewCourses();
        if (idx < 0 || idx >= courses.size()) {
            System.out.println("Invalid!");
            return;
        }
        Course course = courses.get(idx);
        try {
            boolean ok = controller.registerForCourse(student, course);
            if (ok) System.out.println("Successfully registered for: " + course.getName());
        } catch (CreditLimitException | MaxFailedReachedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void showTranscript() {
        System.out.println("\n=== TRANSCRIPT ===");
        java.util.Map<Course, Mark> transcript = controller.viewTranscript(student);
        if (transcript.isEmpty()) {
            System.out.println("No marks yet.");
            return;
        }
        for (java.util.Map.Entry<Course, Mark> entry : transcript.entrySet()) {
            System.out.println(entry.getKey().getName() + " → " + entry.getValue());
        }
        System.out.println("GPA: " + String.format("%.2f", student.getGPA()));
        System.out.println("Total credits: " + student.getCurrentCredits());
    }

    public void showNews() {
        System.out.println("\n=== NEWS ===");
        java.util.List<News> newsList = DataStore.getInstance().getNews();
        newsList.stream().filter(News::getIsPinned).forEach(n -> System.out.println(n));
        newsList.stream().filter(n -> !n.getIsPinned()).forEach(n -> System.out.println(n));
    }

    public void viewSchedule() {
        controllers.ScheduleController sc = new controllers.ScheduleController();
        java.util.List<Schedule> schedules = sc.getScheduleForStudent(student);
        System.out.println("\n=== MY WEEKLY SCHEDULE ===");
        System.out.println(sc.printWeeklySchedule(schedules));
    }

    public void viewRecommendationLetters() {
        System.out.println("=== MY RECOMMENDATION LETTERS ===");
        boolean found = false;
        for (RecommendationLetter l : DataStore.getInstance().getLetters()) {
            if (l.getStudent().equals(student)) {
                System.out.println(l);
                found = true;
            }
        }
        if (!found) System.out.println("No recommendation letters yet.");
    }

}
