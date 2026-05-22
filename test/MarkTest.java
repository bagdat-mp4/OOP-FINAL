package test;

import models.Mark;

public class MarkTest {
    public static void test() {
        System.out.println("\n=== MarkTest ===");

        Mark markA = new Mark();
        markA.setFirstAttestation(30);
        markA.setSecondAttestation(30);
        markA.setFinalExam(40);
        double totalA = markA.getTotal();
        assert Math.abs(totalA - 34.0) < 0.01 : "FAIL: Total calculation wrong, got " + totalA;
        assert totalA < 60 : "FAIL: This should be grade F";
        System.out.println("PASS: getTotal() calculation correct");

        Mark markAReallyA = new Mark();
        markAReallyA.setFirstAttestation(95);
        markAReallyA.setSecondAttestation(95);
        markAReallyA.setFinalExam(95);
        assert markAReallyA.getLetter().equals("A") : "FAIL: Grade A calculation wrong";
        System.out.println("PASS: getLetter() for grade A");

        Mark markB = new Mark();
        markB.setFirstAttestation(85);
        markB.setSecondAttestation(85);
        markB.setFinalExam(85);
        double totalB = markB.getTotal();
        assert totalB >= 80 && totalB < 90 : "FAIL: Grade B total wrong, got " + totalB;
        assert markB.getLetter().equals("B") : "FAIL: Grade B calculation wrong";
        System.out.println("PASS: getLetter() for grade B");

        Mark markC = new Mark();
        markC.setFirstAttestation(75);
        markC.setSecondAttestation(75);
        markC.setFinalExam(75);
        double totalC = markC.getTotal();
        assert totalC >= 70 && totalC < 80 : "FAIL: Grade C total wrong, got " + totalC;
        assert markC.getLetter().equals("C") : "FAIL: Grade C calculation wrong";
        System.out.println("PASS: getLetter() for grade C");

        Mark markD = new Mark();
        markD.setFirstAttestation(65);
        markD.setSecondAttestation(65);
        markD.setFinalExam(65);
        double totalD = markD.getTotal();
        assert totalD >= 60 && totalD < 70 : "FAIL: Grade D total wrong, got " + totalD;
        assert markD.getLetter().equals("D") : "FAIL: Grade D calculation wrong";
        System.out.println("PASS: getLetter() for grade D");

        Mark markF = new Mark();
        markF.setFirstAttestation(45);
        markF.setSecondAttestation(45);
        markF.setFinalExam(45);
        double totalF = markF.getTotal();
        assert totalF < 60 : "FAIL: Grade F total wrong, got " + totalF;
        assert markF.getLetter().equals("F") : "FAIL: Grade F calculation wrong";
        System.out.println("PASS: getLetter() for grade F");
    }
}
