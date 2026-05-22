package views;

import controllers.ResearcherController;
import core.DataStore;
import exceptions.NotAResearcherException;
import models.ResearchPaper;
import models.ResearchProject;
import models.ResearcherDecorator;
import models.User;

import java.util.Date;
import java.util.List;


public class ResearcherView extends BaseView {

    private final User user;
    private final ResearcherController controller = new ResearcherController();

    public ResearcherView(User user) {
        this.user = user;
    }

    @Override
    public void displayMenu() {
        while (true) {
            ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
            System.out.println("\n=== RESEARCHER MENU === [" + user.getFirstName() + "]");
            System.out.println("H-Index: " + (rd != null ? (int) rd.calculateHIndex() : 0));
            System.out.println("Papers:  " + (rd != null ? rd.getPapers().size() : 0));
            System.out.println("1. Add research paper");
            System.out.println("2. View papers (sort by citations)");
            System.out.println("3. View papers (sort by date)");
            System.out.println("4. View papers (sort by pages)");
            System.out.println("5. Create research project");
            System.out.println("6. View my research projects");
            System.out.println("7. Print citation (PLAIN TEXT)");
            System.out.println("8. Print citation (BibTeX)");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: addPaper(); break;
                case 2: printPapers("citations"); break;
                case 3: printPapers("date"); break;
                case 4: printPapers("pages"); break;
                case 5: createProject(); break;
                case 6: viewProjects(); break;
                case 7: printCitation("PLAIN_TEXT"); break;
                case 8: printCitation("BIBTEX"); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    public void addPaper() {
        System.out.print("Title: ");     String title   = readString();
        System.out.print("Journal: ");   String journal = readString();
        System.out.print("DOI: ");       String doi     = readString();
        System.out.print("Pages (>0): "); int pages     = readInt();
        System.out.print("Citations (>=0): "); int citations = readInt();

        ResearchPaper paper = new ResearchPaper(title, journal, doi, pages, new Date());
        paper.setCitations(citations);

        String error = controller.validatePaper(paper);
        if (!error.isEmpty()) {
            System.out.println("ERROR: " + error);
            return;
        }
        if (controller.addResearchPaper(user, paper)) {
            System.out.println("Paper published! News created automatically.");
        } else {
            System.out.println("Error: you are not a researcher.");
        }
    }

    public void printPapers(String criteria) {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
        if (rd == null || rd.getPapers().isEmpty()) {
            System.out.println("No papers yet.");
            return;
        }
        List<ResearchPaper> sorted = controller.getSortedPapers(rd.getPapers(), criteria);
        System.out.println("=== PAPERS (sorted by " + criteria + ") ===");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println((i + 1) + ". " + sorted.get(i));
        }
    }

    public void createProject() {
        System.out.print("Project topic: "); String topic = readString();
        ResearchProject p = controller.createResearchProject(topic);
        try {
            controller.joinProject(p, user);
            System.out.println("Project created: " + topic);
        } catch (NotAResearcherException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void viewProjects() {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
        if (rd == null || rd.getProjects().isEmpty()) {
            System.out.println("No projects yet.");
            return;
        }
        System.out.println("=== MY RESEARCH PROJECTS ===");
        rd.getProjects().forEach(System.out::println);
    }

    public void printCitation(String format) {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
        if (rd == null || rd.getPapers().isEmpty()) {
            System.out.println("No papers.");
            return;
        }
        printPapers("citations");
        System.out.print("Select paper number: ");
        int idx = readInt() - 1;
        List<ResearchPaper> papers = controller.getSortedPapers(rd.getPapers(), "citations");
        if (idx < 0 || idx >= papers.size()) { System.out.println("Invalid!"); return; }
        System.out.println(papers.get(idx).getCitation(enums.CitationFormat.valueOf(format)));
    }
}
