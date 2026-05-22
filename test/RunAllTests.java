package test;

public class RunAllTests {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("  KBTU University System - Unit Tests");
        System.out.println("============================================\n");

        try {
            AuthTest.main(args);
            System.out.println();
        } catch (AssertionError e) {
            System.out.println("FAILED: AuthTest - " + e.getMessage());
        }

        try {
            MarkTest.main(args);
            System.out.println();
        } catch (AssertionError e) {
            System.out.println("FAILED: MarkTest - " + e.getMessage());
        }

        try {
            StudentTest.main(args);
            System.out.println();
        } catch (AssertionError e) {
            System.out.println("FAILED: StudentTest - " + e.getMessage());
        }

        System.out.println("============================================");
        System.out.println("  All tests completed!");
        System.out.println("============================================");
    }
}
