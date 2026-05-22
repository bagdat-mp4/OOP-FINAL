package views;

import controllers.GraduateStudentController;
import core.DataStore;
import exceptions.LowHIndexException;
import models.GraduateStudent;
import models.ResearchPaper;
import models.User;

import java.util.Date;


public class GraduateStudentView extends StudentView {

    private final GraduateStudent gradStudent;
    private final GraduateStudentController controller = new GraduateStudentController();

    public GraduateStudentView(GraduateStudent s) {
        super(s);
        this.gradStudent = s;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== GRADUATE STUDENT MENU === [" + gradStudent.getFirstName() + "]");
            System.out.println("1. View available courses");
            System.out.println("2. Register for course");
            System.out.println("3. View my transcript");
            System.out.println("4. View news");
            System.out.println("5. Change language");
            System.out.println("6. View my schedule");
            System.out.println("7. View recommendation letters");
            System.out.println("8. Rate teacher");
            System.out.println("9. Set supervisor");
            System.out.println("10. View diploma projects");
            System.out.println("11. Add diploma project");
            System.out.println("0. Logout");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1:  showCourseRegisterForm(); break;
                case 2:  registerCourse(); break;
                case 3:  showTranscript(); break;
                case 4:  showNews(); break;
                case 5:  changeLanguageMenu(); break;
                case 6:  viewSchedule(); break;
                case 7:  viewRecommendationLetters(); break;
                case 8:  rateTeacher(); break;
                case 9:  setSupervisor(); break;
                case 10: showDiplomaProjectInfo(); break;
                case 11: addDiplomaProject(); break;
                case 0:  return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    public void setSupervisor() {
        System.out.print("Supervisor email: ");
        String email = readString();
        User sup = DataStore.getInstance().findUserByEmail(email);
        if (sup == null || !DataStore.getInstance().isResearcher(sup)) {
            System.out.println("Supervisor not found or not a researcher!");
            return;
        }
        try {
            gradStudent.setSupervisor(DataStore.getInstance().getResearcher(sup));
            System.out.println("Supervisor set!");
        } catch (LowHIndexException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void showDiplomaProjectInfo() {
        System.out.println("\n=== DIPLOMA PROJECTS ===");
        if (gradStudent.getDiplomaProjects().isEmpty()) {
            System.out.println("  None yet.");
        } else {
            gradStudent.getDiplomaProjects().forEach(p -> System.out.println("  - " + p));
        }
    }

    public void addDiplomaProject() {
        if (gradStudent.getSupervisor() == null) {
            System.out.println("You must set a supervisor before adding a diploma project.");
            return;
        }
        System.out.print("Project title: ");       String title   = readString();
        System.out.print("Journal/Conference: ");  String journal = readString();
        System.out.print("DOI: ");                 String doi     = readString();
        System.out.print("Pages (>0): ");          int pages      = readInt();
        if (title.isBlank() || journal.isBlank() || doi.isBlank() || pages <= 0) {
            System.out.println("All fields required and pages must be > 0.");
            return;
        }
        gradStudent.getDiplomaProjects().add(new ResearchPaper(title, journal, doi, pages, new Date()));
        System.out.println("Diploma project added: " + title);
    }
}
