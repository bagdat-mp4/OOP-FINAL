package views;

import controllers.AdminController;
import models.Admin;

/**
 * Admin view with console menu
 */
public class AdminView extends BaseView {

    private Admin admin;
    private AdminController controller = new AdminController();

    public AdminView(Admin admin) {
        this.admin = admin;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU === [" + admin.getFirstName() + "]");
            System.out.println("1. View all users");
            System.out.println("2. Create user");
            System.out.println("3. Remove user");
            System.out.println("4. View log files");
            System.out.println("5. Advanced search (regex)");
            System.out.println("6. Change language");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: showAllUsers(); break;
                case 2: showNewUserForm(); break;
                case 3: removeUser(); break;
                case 4: showLogFiles(); break;
                case 5: advancedSearch(); break;
                case 6: changeLanguageMenu(); break;
                case 0: return;
            }
        }
    }

    public void showAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        controller.getAllUsers().forEach(u -> System.out.println(u));
    }

    public void showNewUserForm() {
        System.out.print("First name: "); String fn = readString();
        System.out.print("Last name: "); String ln = readString();
        System.out.print("Email: "); String email = readString();
        System.out.println("Role (STUDENT/TEACHER/MANAGER/TECHSUPPORT/GRADUATESTUDENT): ");
        String role = readString();
        boolean ok = controller.createUser(email, fn, ln, role);
        System.out.println(ok ? "User created! Default password: password123" : "Failed!");
    }

    public void removeUser() {
        System.out.print("Enter user email to remove: ");
        boolean ok = controller.removeUser(readString());
        System.out.println(ok ? "User removed!" : "User not found!");
    }

    public void showLogFiles() {
        System.out.println("\n=== LOG FILES ===");
        controller.getLogFiles().forEach(System.out::println);
    }

    public void advancedSearch() {
        System.out.println("=== ADVANCED SEARCH ===");
        System.out.println("1. Search users\n2. Search courses\n3. Search papers");
        System.out.print("Choose: ");
        int choice = readInt();
        System.out.print("Enter regex pattern (e.g. 'aibek|ainur' or '^A'): ");
        String pattern = readString();

        controllers.SearchController sc = new controllers.SearchController();
        if (choice == 1) {
            System.out.println("Results:");
            sc.searchUsersByRegex(pattern).forEach(u -> System.out.println("  " + u));
        } else if (choice == 2) {
            sc.searchCoursesByRegex(pattern).forEach(c -> System.out.println("  " + c));
        } else if (choice == 3) {
            sc.searchPapersByRegex(pattern).forEach(p -> System.out.println("  " + p));
        }
    }

}
