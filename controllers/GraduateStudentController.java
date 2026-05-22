package controllers;

import core.DataStore;
import models.*;

/**
 * Graduate student controller
 */
public class GraduateStudentController extends controllers.StudentController {

    public GraduateStudentController() {
    }

    public boolean setDissertationTheme(models.GraduateStudent student, String theme) {
        System.out.println("Dissertation theme set for " + student.getFirstName() + ": " + theme);
        return true;
    }

}
