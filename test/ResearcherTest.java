package test;

import models.Teacher;
import models.ResearcherDecorator;
import models.ResearchPaper;
import models.GraduateStudent;
import exceptions.LowHIndexException;
import comparators.CitationsComparator;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ResearcherTest {
    public static void test() {
        System.out.println("\n=== ResearcherTest ===");

        Teacher teacher = new Teacher(200L, "Prof", "Test", "prof@uni.kz", "pass", 300000);
        ResearcherDecorator rd = new ResearcherDecorator(teacher);

        ResearchPaper p1 = new ResearchPaper("Paper1", "Journal1", "doi1", 10, new Date());
        p1.setCitations(5);
        ResearchPaper p2 = new ResearchPaper("Paper2", "Journal2", "doi2", 8, new Date());
        p2.setCitations(3);
        ResearchPaper p3 = new ResearchPaper("Paper3", "Journal3", "doi3", 6, new Date());
        p3.setCitations(1);

        rd.addPaper(p1);
        rd.addPaper(p2);
        rd.addPaper(p3);

        double hIndex = rd.calculateHIndex();
        assert hIndex == 2.0 : "FAIL: h-index should be 2, got " + hIndex;
        System.out.println("PASS: calculateHIndex() = 2");

        Teacher lowTeacher = new Teacher(201L, "Low", "Teacher", "low@uni.kz", "pass", 200000);
        ResearcherDecorator lowRd = new ResearcherDecorator(lowTeacher);

        GraduateStudent gradStudent = new GraduateStudent(300L, "Grad", "Student", "grad2@uni.kz", "pass", "CS", 1);
        boolean threw = false;
        try {
            gradStudent.setSupervisor(lowRd);
        } catch (LowHIndexException e) {
            threw = true;
            System.out.println("PASS: LowHIndexException when h-index < 3");
        }
        assert threw : "FAIL: LowHIndexException not thrown";

        List<ResearchPaper> papers = new ArrayList<>(Arrays.asList(p1, p2, p3));
        papers.sort(new CitationsComparator());
        assert papers.get(0).getCitations() >= papers.get(1).getCitations() : "FAIL: Sort by citations";
        System.out.println("PASS: printPapers sorting by citations");
    }
}
