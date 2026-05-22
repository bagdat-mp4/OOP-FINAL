package views;

import controllers.*;
import models.*;
import core.DataStore;
import enums.*;


public class ManagerView extends BaseView {

    private Manager manager;
    private ManagerController controller = new ManagerController();

    public ManagerView(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== MANAGER MENU === [" + manager.getFirstName() + "]");
            System.out.println("1. Add course");
            System.out.println("2. Assign course to teacher");
            System.out.println("3. Generate academic report");
            System.out.println("4. Make user a researcher");
            System.out.println("5. Change language");
            System.out.println("6. Send official message");
            System.out.println("7. Generate course schedule");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: newCourseForm(); break;
                case 2: assignCourseToTeacher(); break;
                case 3: generateAcademicReports(); break;
                case 4: makeResearcher(); break;
                case 5: changeLanguageMenu(); break;
                case 6: sendOfficialMessage(); break;
                case 7: generateSchedule(); break;
                case 0: return;
            }
        }
    }

    public void newCourseForm() {
        System.out.print("Course name: "); String name = readString();
        System.out.print("Course code: "); String code = readString();
        System.out.print("Credits: "); int credits = readInt();
        System.out.print("Target year: "); int year = readInt();
        System.out.println("Type (MAJOR/MINOR/FREE_ELECTIVE): ");
        CourseType type = CourseType.valueOf(readString().toUpperCase());
        controller.addCourse(name, code, credits, year, type);
        System.out.println("Course added!");
    }

    public void assignCourseToTeacher() {
        System.out.print("Course code: "); String code = readString();
        System.out.print("Teacher email: "); String email = readString();
        Course course = null;
        for (Course c : DataStore.getInstance().getCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                course = c;
                break;
            }
        }
        User u = DataStore.getInstance().findUserByEmail(email);
        if (course == null || !(u instanceof Teacher)) {
            System.out.println("Not found!");
            return;
        }
        controller.assignCourse(course, (Teacher) u);
        System.out.println("Course assigned!");
    }

    public void generateAcademicReports() {
        System.out.println(controller.createAcademicReport());
    }

    public void makeResearcher() {
        System.out.print("User email: "); String email = readString();
        User u = DataStore.getInstance().findUserByEmail(email);
        if (u == null) {
            System.out.println("Not found!");
            return;
        }
        controller.makeResearcher(u);
        System.out.println(u.getFirstName() + " is now a Researcher!");
    }

    public void sendOfficialMessage() {
        System.out.print("Message: ");
        String msg = readString();
        GlobalMessage gm = new GlobalMessage(manager, msg);
        DataStore.getInstance().addMessage(new Message(manager, manager, msg));
        System.out.println("Official message sent!");
    }

    public void generateSchedule() {
        System.out.print("Course code: ");
        String code = readString();
        Course course = null;
        for (Course c : DataStore.getInstance().getCourses())
            if (c.getCourseCode().equalsIgnoreCase(code)) { course = c; break; }
        if (course == null) { System.out.println("Course not found!"); return; }

        System.out.print("Teacher email: ");
        User u = DataStore.getInstance().findUserByEmail(readString());
        if (!(u instanceof Teacher)) { System.out.println("Teacher not found!"); return; }

        controllers.ScheduleController sc = new controllers.ScheduleController();
        Schedule lecture = sc.generateSchedule(course, (Teacher)u, enums.LessonType.LECTURE);
        Schedule practice = sc.generateSchedule(course, (Teacher)u, enums.LessonType.PRACTICE);

        if (lecture != null) System.out.println("Lecture: " + lecture);
        if (practice != null) System.out.println("Practice: " + practice);
        System.out.println("Schedule generated!");
    }

}
