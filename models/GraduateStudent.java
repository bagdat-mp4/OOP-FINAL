package models;

import exceptions.LowHIndexException;

import java.io.*;
import java.util.*;

/**
 * Graduate student class
 */
public class GraduateStudent extends Student {

    private Researcher supervisor;
    private List<ResearchPaper> diplomaProjects;

    /**
     * Default constructor
     */
    public GraduateStudent() {
        super();
        this.diplomaProjects = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     */
    public GraduateStudent(long id, String firstName, String lastName, String email, String password, String major, int yearOfStudy) {
        super(id, firstName, lastName, email, password, major, yearOfStudy);
        this.diplomaProjects = new ArrayList<>();
    }

    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
        if (supervisor.calculateHIndex() < 3) {
            throw new LowHIndexException("Supervisor H-index must be at least 3. Current: " + supervisor.calculateHIndex());
        }
        this.supervisor = supervisor;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public List<ResearchPaper> getDiplomaProjects() {
        return diplomaProjects;
    }

    public void setDiplomaProjects(List<ResearchPaper> diplomaProjects) {
        this.diplomaProjects = diplomaProjects;
    }

    public String getFirstName() {
        return super.getFirstName();
    }
}