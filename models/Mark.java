package models;

import java.io.*;
import java.util.*;


public class Mark implements Serializable {

    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    
    public Mark() {
    }

    
    public double getTotal() {
        return firstAttestation * 0.3 + secondAttestation * 0.3 + finalExam * 0.4;
    }

    
    public String getLetter() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 80) return "B";
        if (total >= 70) return "C";
        if (total >= 60) return "D";
        return "F";
    }

    public void setFirstAttestation(double firstAttestation) {
        this.firstAttestation = firstAttestation;
    }

    public void setSecondAttestation(double secondAttestation) {
        this.secondAttestation = secondAttestation;
    }

    public void setFinalExam(double finalExam) {
        this.finalExam = finalExam;
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }

    public double getFinalExam() {
        return finalExam;
    }

    @Override
    public String toString() {
        return "[AT1=" + firstAttestation + " AT2=" + secondAttestation +
               " Final=" + finalExam + " Total=" + String.format("%.2f", getTotal()) +
               " (" + getLetter() + ")]";
    }

}