package views;

import controllers.*;
import models.*;
import core.DataStore;
import exceptions.*;


public class GraduateStudentView extends StudentView {

    private GraduateStudent gradStudent;
    private GraduateStudentController controller = new GraduateStudentController();

    public GraduateStudentView(GraduateStudent s) {
        super(s);
        this.gradStudent = s;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== GRADUATE STUDENT MENU === [" + gradStudent.getFirstName() + "]");
            System.out.println("1. Student menu");
            System.out.println("2. Set supervisor");
            System.out.println("3. View diploma projects");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: super.displayMenu(); break;
                case 2: setSupervisor(); break;
                case 3: showDiplomaProjectInfo(); break;
                case 0: return;
            }
        }
    }

    public void setSupervisor() {
        System.out.print("Supervisor email: ");
        java.util.Optional<User> sup = DataStore.getInstance().getUsers().stream()
            .filter(u -> u.getEmail().equals(readString())).findFirst();
        if (sup.isEmpty() || !(DataStore.getInstance().isResearcher(sup.get()))) {
            System.out.println("Supervisor not found or not a researcher!");
            return;
        }
        try {
            gradStudent.setSupervisor(DataStore.getInstance().getResearcher(sup.get()));
            System.out.println("Supervisor set!");
        } catch (LowHIndexException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void showDiplomaProjectInfo() {
        System.out.println("Diploma Projects:");
        if (gradStudent.getDiplomaProjects().isEmpty()) {
            System.out.println("  None yet.");
        } else {
            gradStudent.getDiplomaProjects().forEach(p -> System.out.println("  - " + p));
        }
    }

}
