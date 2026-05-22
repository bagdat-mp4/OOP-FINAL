package test;

import models.Mark;

public class MarkTest {
    public static void main(String[] args) {
        System.out.println("=== Mark Tests ===");

        // Test 1: getTotal calculation
        Mark mark = new Mark();
        mark.setFirstAttestation(80);
        mark.setSecondAttestation(75);
        mark.setFinalExam(90);
        double expected = 80*0.3 + 75*0.3 + 90*0.4; // = 82.5
        assert Math.abs(mark.getTotal() - 82.5) < 0.01 : "FAIL: getTotal()";
        System.out.println("PASS: getTotal() = " + mark.getTotal());

        // Test 2: getLetter A
        Mark markA = new Mark();
        markA.setFirstAttestation(95);
        markA.setSecondAttestation(95);
        markA.setFinalExam(95);
        assert markA.getLetter().equals("A") : "FAIL: getLetter A";
        System.out.println("PASS: getLetter() = " + markA.getLetter());

        // Test 3: getLetter F
        Mark markF = new Mark();
        markF.setFirstAttestation(30);
        markF.setSecondAttestation(30);
        markF.setFinalExam(30);
        assert markF.getLetter().equals("F") : "FAIL: getLetter F";
        System.out.println("PASS: getLetter() = " + markF.getLetter());

        System.out.println("All Mark tests passed!");
    }
}
