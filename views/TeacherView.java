package views;

import controllers.*;
import models.*;
import enums.*;
import core.DataStore;

/**
 * Teacher view with console menu
 */
public class TeacherView extends BaseView {

    private Teacher teacher;
    private TeacherController controller = new TeacherController();

    public TeacherView(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== TEACHER MENU === [" + teacher.getFirstName() + "]");
            System.out.println("1. View my courses");
            System.out.println("2. View students in course");
            System.out.println("3. Put mark");
            System.out.println("4. View news");
            System.out.println("5. Change language");
            System.out.println("6. Generate mark report");
            System.out.println("7. Mark attendance");
            System.out.println("8. View attendance report");
            System.out.println("9. Write recommendation letter");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            int c = readInt();
            switch (c) {
                case 1: showCoursesAndStudents(); break;
                case 2: viewStudentsInCourse(); break;
                case 3: addMarkToStudent(); break;
                case 4: viewNews(); break;
                case 5: changeLanguageMenu(); break;
                case 6: generateMarkReport(); break;
                case 7: markAttendance(); break;
                case 8: viewAttendanceReport(); break;
                case 9: writeRecommendationLetter(); break;
                case 0: return;
            }
        }
    }

    public void showCoursesAndStudents() {
        System.out.println("\n=== MY COURSES ===");
        for (Course c : controller.viewCourses(teacher)) System.out.println(c);
    }

    public void viewStudentsInCourse() {
        System.out.print("Course code: ");
        String code = readString();
        for (Course c : teacher.getActiveCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                System.out.println("Students in " + c.getName() + ":");
                controller.viewStudents(c).forEach(s -> System.out.println("  - " + s));
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public void addMarkToStudent() {
        System.out.print("Student email: ");
        User u = DataStore.getInstance().findUserByEmail(readString());
        if (!(u instanceof Student)) {
            System.out.println("Student not found!");
            return;
        }
        System.out.print("Course code: ");
        String code = readString();
        Course found = null;
        for (Course c : teacher.getActiveCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                found = c;
                break;
            }
        }
        if (found == null) {
            System.out.println("You don't teach this course.");
            return;
        }
        System.out.print("Type (AT1/AT2/FINAL): ");
        String type = readString();
        System.out.print("Score: ");
        double score = Double.parseDouble(readString());
        controller.putMark((Student) u, found, type, score);
        System.out.println("Mark saved!");
    }

    public void viewNews() {
        DataStore.getInstance().getNews().forEach(n -> System.out.println(n));
    }

    public void generateMarkReport() {
        System.out.println(controller.generateMarkReport(teacher));
    }

    public void markAttendance() {
        System.out.print("Course code: ");
        String code = readString();
        Course course = null;
        for (Course c : teacher.getActiveCourses())
            if (c.getCourseCode().equalsIgnoreCase(code)) { course = c; break; }
        if (course == null) { System.out.println("Not found!"); return; }

        System.out.println("Mark attendance for " + course.getName() + ":");
        for (Student s : course.getEnrolledStudents()) {
            System.out.print(s.getFirstName() + " " + s.getLastName() + " (present? y/n): ");
            boolean present = readString().equalsIgnoreCase("y");
            controller.markAttendance(course, s, present);
        }
        System.out.println("Attendance marked!");
    }

    public void viewAttendanceReport() {
        System.out.print("Course code: ");
        String code = readString();
        for (Course c : teacher.getActiveCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                System.out.println(controller.getAttendanceReport(c));
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public void writeRecommendationLetter() {
        System.out.print("Student email: ");
        User u = DataStore.getInstance().findUserByEmail(readString());
        if (!(u instanceof Student)) { System.out.println("Student not found!"); return; }
        Student student = (Student) u;

        System.out.println("Purpose (Graduate School/Scholarship/Job): ");
        String purpose = readString();

        String content = "I am writing to recommend " + student.getFirstName() + " " +
            student.getLastName() + " for " + purpose + ".\n\n" +
            "During their studies, " + student.getFirstName() + " has demonstrated " +
            "exceptional dedication and academic excellence. " +
            "Their current GPA is " + String.format("%.2f", student.getGPA()) + "/4.0, " +
            "placing them among the top students in their cohort.\n\n" +
            "I highly recommend " + student.getFirstName() + " without any reservation.";

        RecommendationLetter letter = new RecommendationLetter(teacher, student, content, purpose);
        DataStore.getInstance().addLetter(letter);
        System.out.println(letter);
        System.out.println("Letter saved!");
    }

}
