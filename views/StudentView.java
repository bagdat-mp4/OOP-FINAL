package views;

import controllers.EmployeeMessageController;
import controllers.ScheduleController;
import controllers.StudentController;
import core.DataStore;
import exceptions.CreditLimitException;
import exceptions.MaxFailedReachedException;
import models.Course;
import models.Mark;
import models.News;
import models.RecommendationLetter;
import models.Schedule;
import models.Student;
import models.TechSupportRequest;

import java.util.List;
import java.util.Map;


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
            System.out.println("8. Call tech support");
            System.out.println("9. Rate teacher");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: showCourseRegisterForm(); break;
                case 2: registerCourse(); break;
                case 3: showTranscript(); break;
                case 4: showNews(); break;
                case 5: changeLanguageMenu(); break;
                case 6: viewSchedule(); break;
                case 7: viewRecommendationLetters(); break;
                case 8: callTechSupport(); break;
                case 9: rateTeacher(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    public void showCourseRegisterForm() {
        System.out.println("\n=== AVAILABLE COURSES ===");
        List<Course> courses = controller.viewCourses();
        List<Course> enrolled = controller.getEnrolledCourses(student);
        for (int i = 0; i < courses.size(); i++) {
            String tag = enrolled.contains(courses.get(i)) ? " [ENROLLED]" : "";
            System.out.println((i + 1) + ". " + courses.get(i) + tag);
        }
    }

    public void registerCourse() {
        showCourseRegisterForm();
        System.out.print("Enter course number: ");
        int idx = readInt() - 1;
        List<Course> courses = controller.viewCourses();
        if (idx < 0 || idx >= courses.size()) {
            System.out.println("Invalid!");
            return;
        }
        try {
            if (controller.registerForCourse(student, courses.get(idx))) {
                System.out.println("Successfully registered for: " + courses.get(idx).getName());
            }
        } catch (CreditLimitException | MaxFailedReachedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void showTranscript() {
        System.out.println("\n=== TRANSCRIPT ===");
        Map<Course, Mark> transcript = controller.viewTranscript(student);
        if (transcript.isEmpty()) {
            System.out.println("No marks yet.");
            return;
        }
        for (Map.Entry<Course, Mark> entry : transcript.entrySet()) {
            System.out.println(entry.getKey().getName() + " -> " + entry.getValue());
        }
        System.out.println("GPA: " + String.format("%.2f", student.getGPA()));
        System.out.println("Total credits: " + student.getCurrentCredits());
    }

    public void showNews() {
        System.out.println("\n=== NEWS ===");
        List<News> newsList = DataStore.getInstance().getNews();
        newsList.stream().filter(News::isPinned).forEach(System.out::println);
        newsList.stream().filter(n -> !n.isPinned()).forEach(System.out::println);
    }

    public void viewSchedule() {
        ScheduleController sc = new ScheduleController();
        List<Schedule> schedules = sc.getScheduleForStudent(student);
        System.out.println("\n=== MY WEEKLY SCHEDULE ===");
        System.out.println(sc.printWeeklySchedule(schedules));
    }

    public void rateTeacher() {
        List<Course> enrolled = controller.getEnrolledCourses(student);
        if (enrolled.isEmpty()) { System.out.println("You are not enrolled in any courses."); return; }
        System.out.println("\n=== RATE TEACHER ===");
        for (int i = 0; i < enrolled.size(); i++) {
            Course c = enrolled.get(i);
            String teacher = c.getLectureInstructors().isEmpty() ? "No teacher"
                : c.getLectureInstructors().get(0).getFirstName() + " " + c.getLectureInstructors().get(0).getLastName();
            System.out.println((i + 1) + ". " + c.getName() + " — " + teacher);
        }
        System.out.print("Select course: ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= enrolled.size()) { System.out.println("Invalid!"); return; }
        Course course = enrolled.get(idx);
        if (course.getLectureInstructors().isEmpty()) { System.out.println("No teacher assigned."); return; }
        System.out.print("Rating (1-5): ");
        int rating = readInt();
        if (controller.rateTeacher(course.getLectureInstructors().get(0), rating)) {
            System.out.println("Rating submitted!");
        } else {
            System.out.println("Invalid rating. Must be 1-5.");
        }
    }

    public void callTechSupport() {
        System.out.print("Describe your issue: ");
        String issue = readString();
        if (issue.isBlank()) { System.out.println("Issue cannot be empty."); return; }
        new EmployeeMessageController().callSupport(new TechSupportRequest(student, issue));
        System.out.println("Request submitted successfully!");
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
