package views;

import controllers.EmployeeMessageController;
import controllers.ManagerController;
import controllers.ScheduleController;
import core.DataStore;
import enums.CourseType;
import enums.LessonType;
import models.Course;
import models.Manager;
import models.Schedule;
import models.Teacher;
import models.TechSupportRequest;
import models.User;


public class ManagerView extends BaseView {

    private final Manager manager;
    private final ManagerController controller = new ManagerController();

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
            System.out.println("6. Broadcast message to all staff");
            System.out.println("7. Generate course schedule");
            System.out.println("8. View inbox");
            System.out.println("9. View complaints");
            System.out.println("10. Call tech support");
            System.out.println("11. Send message to employee");
            System.out.println("12. Approve student registration");
            System.out.println("13. Researcher requests");
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
                case 8: viewInbox(); break;
                case 9:  viewComplaints(); break;
                case 10: callTechSupport(); break;
                case 11: sendMessage(); break;
                case 12: approveRegistration(); break;
                case 13: viewResearcherRequests(); break;
                case 0:  return;
                default: System.out.println("Invalid choice!");
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
        System.out.print("Broadcast message: ");
        String msg = readString();
        controller.broadcastMessage(manager, msg);
        System.out.println("Broadcast sent to all staff!");
    }

    public void viewInbox() {
        System.out.println("\n=== INBOX ===");
        if (manager.getInbox().isEmpty()) {
            System.out.println("No messages.");
            return;
        }
        manager.getInbox().forEach(System.out::println);
    }

    public void sendMessage() {
        System.out.print("Recipient email: ");
        User receiver = DataStore.getInstance().findUserByEmail(readString());
        if (!(receiver instanceof models.Employee)) { System.out.println("Employee not found!"); return; }
        System.out.print("Message: ");
        String text = readString();
        new controllers.EmployeeMessageController().sendEmployeeMessage(manager, receiver, text);
        System.out.println("Message sent!");
    }

    public void viewResearcherRequests() {
        System.out.println("\n=== RESEARCHER REQUESTS ===");
        java.util.List<models.User> requests = controller.getResearcherRequests();
        if (requests.isEmpty()) { System.out.println("No pending requests."); return; }
        for (int i = 0; i < requests.size(); i++) {
            models.User u = requests.get(i);
            System.out.println((i + 1) + ". " + u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")");
        }
        System.out.print("Select number (0 to cancel): ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= requests.size()) return;
        models.User u = requests.get(idx);
        System.out.println("1. Accept  2. Reject");
        System.out.print("Choose: ");
        int choice = readInt();
        if (choice == 1) {
            controller.acceptResearcherRequest(u);
            System.out.println(u.getFirstName() + " is now a Researcher!");
        } else if (choice == 2) {
            controller.rejectResearcherRequest(u);
            System.out.println("Request rejected.");
        }
    }

    public void approveRegistration() {
        System.out.print("Student email: ");
        User s = DataStore.getInstance().findUserByEmail(readString());
        if (!(s instanceof models.Student)) { System.out.println("Student not found!"); return; }
        System.out.print("Course code: ");
        String code = readString();
        models.Course course = null;
        for (models.Course c : DataStore.getInstance().getCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) { course = c; break; }
        }
        if (course == null) { System.out.println("Course not found!"); return; }
        controller.approveRegistration((models.Student) s, course);
        System.out.println("Registration approved!");
    }

    public void callTechSupport() {
        System.out.print("Describe your issue: ");
        String issue = readString();
        if (issue.isBlank()) { System.out.println("Issue cannot be empty."); return; }
        new EmployeeMessageController().callSupport(new TechSupportRequest(manager, issue));
        System.out.println("Request submitted successfully!");
    }

    public void viewComplaints() {
        System.out.println("\n=== COMPLAINTS ===");
        controller.getComplaints(manager).forEach(System.out::println);
        if (controller.getComplaints(manager).isEmpty()) System.out.println("No complaints.");
    }

    public void generateSchedule() {
        System.out.print("Course code: ");
        String code = readString();
        Course course = null;
        for (Course c : DataStore.getInstance().getCourses()) {
            if (c.getCourseCode().equalsIgnoreCase(code)) { course = c; break; }
        }
        if (course == null) { System.out.println("Course not found!"); return; }

        System.out.print("Teacher email: ");
        User u = DataStore.getInstance().findUserByEmail(readString());
        if (!(u instanceof Teacher)) { System.out.println("Teacher not found!"); return; }

        ScheduleController sc = new ScheduleController();
        Schedule lecture = sc.generateSchedule(course, (Teacher) u, LessonType.LECTURE);
        Schedule practice = sc.generateSchedule(course, (Teacher) u, LessonType.PRACTICE);

        if (lecture != null) System.out.println("Lecture: " + lecture);
        if (practice != null) System.out.println("Practice: " + practice);
        System.out.println("Schedule generated!");
    }
}
