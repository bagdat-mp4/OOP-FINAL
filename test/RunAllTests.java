package test;

public class RunAllTests {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("  KBTU University System - Unit Tests");
        System.out.println("============================================");

        int passed = 0;
        int failed = 0;

        try {
            AuthTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: AuthTest - " + e.getMessage());
            failed++;
        }

        try {
            MarkTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: MarkTest - " + e.getMessage());
            failed++;
        }

        try {
            StudentTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: StudentTest - " + e.getMessage());
            failed++;
        }

        try {
            CourseTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: CourseTest - " + e.getMessage());
            failed++;
        }

        try {
            ResearcherTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: ResearcherTest - " + e.getMessage());
            failed++;
        }

        try {
            TeacherTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: TeacherTest - " + e.getMessage());
            failed++;
        }

        try {
            ManagerTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: ManagerTest - " + e.getMessage());
            failed++;
        }

        try {
            AdminTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: AdminTest - " + e.getMessage());
            failed++;
        }

        try {
            ScheduleTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: ScheduleTest - " + e.getMessage());
            failed++;
        }

        try {
            AttendanceTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: AttendanceTest - " + e.getMessage());
            failed++;
        }

        try {
            RecommendationLetterTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: RecommendationLetterTest - " + e.getMessage());
            failed++;
        }

        try {
            SearchTest.test();
            passed++;
        } catch (AssertionError e) {
            System.out.println("FAIL: SearchTest - " + e.getMessage());
            failed++;
        }

        System.out.println("\n============================================");
        System.out.println("  Tests passed: " + passed);
        System.out.println("  Tests failed: " + failed);
        System.out.println("============================================");
    }
}
