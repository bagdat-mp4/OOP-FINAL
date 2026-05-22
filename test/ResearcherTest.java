package test;

import models.*;
import exceptions.LowHIndexException;
import comparators.*;
import models.ResearchPaper;

import java.util.*;

public class ResearcherTest {
    public static void main(String[] args) {
        System.out.println("=== Researcher Tests ===");

        // Test 1: h-index calculation
        models.Teacher teacher = new models.Teacher(200L, "Prof", "Test", "prof@uni.kz", "pass", 300000);
        models.ResearcherDecorator rd = new ResearcherDecorator(teacher);

        ResearchPaper p1 = new ResearchPaper("Paper1", "Journal1", "doi1", 10, new Date());
        p1.setCitations(5);
        models.ResearchPaper p2 = new models.ResearchPaper("Paper2", "Journal2", "doi2", 8, new Date());
        p2.setCitations(3);
        models.ResearchPaper p3 = new models.ResearchPaper("Paper3", "Journal3", "doi3", 6, new Date());
        p3.setCitations(1);

        rd.addPaper(p1);
        rd.addPaper(p2);
        rd.addPaper(p3);

        // h-index = 2 (2 papers have >= 2 citations: papers with 5 and 3 citations)
        double hIndex = rd.calculateHIndex();
        assert hIndex == 2.0 : "FAIL: h-index should be 2, got " + hIndex;
        System.out.println("PASS: h-index = " + hIndex + " (expected 2)");

        // Test 2: LowHIndexException when supervisor h-index < 3
        models.Teacher lowTeacher = new models.Teacher(201L, "Low", "Teacher", "low@uni.kz", "pass", 200000);
        models.ResearcherDecorator lowRd = new models.ResearcherDecorator(lowTeacher);
        // No papers = h-index 0

        models.GraduateStudent gradStudent = new models.GraduateStudent(300L, "Grad", "Student", "grad2@uni.kz", "pass", "CS", 1);
        boolean threw = false;
        try {
            gradStudent.setSupervisor(lowRd);
        } catch (LowHIndexException e) {
            threw = true;
            System.out.println("PASS: LowHIndexException thrown - " + e.getMessage());
        }
        assert threw : "FAIL: LowHIndexException not thrown";

        // Test 3: Sort papers by citations
        List<ResearchPaper> papers = new ArrayList<>(Arrays.asList(p1, p2, p3));
        papers.sort(new comparators.CitationsComparator());
        assert papers.get(0).getCitations() >= papers.get(1).getCitations() : "FAIL: Sort by citations";
        System.out.println("PASS: Papers sorted by citations: " + papers.get(0).getCitations() + " >= " + papers.get(1).getCitations());

        System.out.println("All Researcher tests passed!");
    }
}
