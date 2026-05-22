package controllers;

import models.GraduateStudent;

public class GraduateStudentController extends StudentController {

    public GraduateStudentController() {
    }

    public boolean setDissertationTheme(GraduateStudent student, String theme) {
        System.out.println("Dissertation theme set for " + student.getFirstName() + ": " + theme);
        return true;
    }

}
