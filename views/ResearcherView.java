package views;

import controllers.*;
import models.*;
import core.DataStore;
import comparators.*;
import enums.*;
import exceptions.*;
import java.util.*;


public class ResearcherView extends BaseView {

    private User user;
    private ResearcherController controller = new ResearcherController();

    public ResearcherView(User user) {
        this.user = user;
    }

    @Override
    public void displayMenu() {
        while (true) {
            ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
            System.out.println("\n=== RESEARCHER MENU ===");
            System.out.println("H-Index: " + (rd != null ? rd.calculateHIndex() : 0));
            System.out.println("1. Add research paper");
            System.out.println("2. View my papers (sort by citations)");
            System.out.println("3. Create research project");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            switch (readInt()) {
                case 1: addPaper(); break;
                case 2: printPapers("citations"); break;
                case 3: createProject(); break;
                case 0: return;
            }
        }
    }

    public void addPaper() {
        System.out.print("Title: "); String title = readString();
        System.out.print("Journal: "); String journal = readString();
        System.out.print("DOI: "); String doi = readString();
        System.out.print("Pages: "); int pages = readInt();
        System.out.print("Citations: "); int citations = readInt();
        ResearchPaper paper = new ResearchPaper(title, journal, doi, pages, new Date());
        paper.setCitations(citations);
        controller.addResearchPaper(user, paper);
        System.out.println("Paper published! News created automatically.");
    }

    public void printPapers(String criteria) {
        ResearcherDecorator rd = DataStore.getInstance().getResearcher(user);
        if (rd == null) {
            System.out.println("Not a researcher.");
            return;
        }
        List<ResearchPaper> sorted = controller.getSortedPapers(rd.getPapers(), criteria);
        System.out.println("=== PAPERS (sorted by " + criteria + ") ===");
        sorted.forEach(p -> System.out.println(p));
    }

    public void createProject() {
        System.out.print("Project topic: "); String topic = readString();
        ResearchProject p = controller.createResearchProject(topic);
        try {
            controller.joinProject(p, user);
        } catch (NotAResearcherException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Project created: " + topic);
    }

}
