package models;

import java.io.Serializable;


public class Mark implements Serializable {

    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;


    public Mark() {}

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public String getLetter() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "F";
    }

    public void setFirstAttestation(double v) { this.firstAttestation = v; }
    public void setSecondAttestation(double v) { this.secondAttestation = v; }
    public void setFinalExam(double v) { this.finalExam = v; }
    public double getFirstAttestation() { return firstAttestation; }
    public double getSecondAttestation() { return secondAttestation; }
    public double getFinalExam() { return finalExam; }

    @Override
    public String toString() {
        return "[AT1=" + firstAttestation + "/30  AT2=" + secondAttestation + "/30" +
               "  Final=" + finalExam + "/40  Total=" + String.format("%.1f", getTotal()) +
               " (" + getLetter() + ")]";
    }
}
