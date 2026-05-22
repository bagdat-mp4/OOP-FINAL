package utils;

import models.*;
import enums.*;


public class UserFactory {

    public static User createUser(UserType type, long id, String firstName, String lastName, String email, String password) {
        switch (type) {
            case STUDENT:
                return new Student(id, firstName, lastName, email, password, "CS", 1);
            case TEACHER:
                return new Teacher(id, firstName, lastName, email, password, 300000);
            case MANAGER:
                return new Manager(id, firstName, lastName, email, password, 400000);
            case TECHSUPPORT:
                return new TechSupportSpecialist(id, firstName, lastName, email, password, 250000);
            case GRADUATESTUDENT:
                return new GraduateStudent(id, firstName, lastName, email, password, "CS", 1);
            default:
                return null;
        }
    }

}