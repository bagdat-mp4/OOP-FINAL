import controllers.AuthController;
import core.DataStore;
import exceptions.InvalidCredentialsException;
import models.*;
import views.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  KBTU University Research System");
        System.out.println("===========================================");

        DataStore ds = DataStore.getInstance();
        AuthController auth = new AuthController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Login\n2. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("2")) {
                ds.save();
                break;
            }

            System.out.print("Email: ");    String email    = scanner.nextLine().trim();
            System.out.print("Password: "); String password = scanner.nextLine().trim();

            User user;
            try {
                user = auth.login(email, password);
            } catch (InvalidCredentialsException e) {
                System.out.println("ERROR: " + e.getMessage());
                continue;
            }
            System.out.println("Welcome, " + user.getFirstName() + "!");
            if (ds.isResearcher(user)) {
                System.out.println("[Researcher status active]");
            }

            launchRoleMenu(user);

            if (ds.isResearcher(user)) {
                System.out.print("\nOpen Researcher mode? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                    new ResearcherView(user).displayMenu();
                }
            }

            ds.save();
        }
    }

    private static void launchRoleMenu(User user) {
        if (user instanceof Admin)                      new AdminView((Admin) user).displayMenu();
        else if (user instanceof GraduateStudent)       new GraduateStudentView((GraduateStudent) user).displayMenu();
        else if (user instanceof Student)               new StudentView((Student) user).displayMenu();
        else if (user instanceof Teacher)               new TeacherView((Teacher) user).displayMenu();
        else if (user instanceof Manager)               new ManagerView((Manager) user).displayMenu();
        else if (user instanceof TechSupportSpecialist) new TechSupportView((TechSupportSpecialist) user).displayMenu();
    }
}
