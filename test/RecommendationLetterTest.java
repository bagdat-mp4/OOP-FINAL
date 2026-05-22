package test;

import models.Teacher;
import models.Student;
import models.RecommendationLetter;
import models.Mark;
import models.Course;
import enums.CourseType;
import enums.TeacherTitle;

public class RecommendationLetterTest {
    public static void test() {
        System.out.println("\n=== RecommendationLetterTest ===");

        Teacher teacher = new Teacher(701L, "Prof", "Smith", "profsmith@test.kz", "pass", 300000);
        teacher.setTitle(TeacherTitle.PROFESSOR);

        Student student = new Student(702L, "John", "Doe", "johndoe@test.kz", "pass", "CS", 3);
        Course course = new Course(703L, "CS101", "Test", 3, CourseType.MAJOR, 1);
        Mark mark = new Mark();
        mark.setFirstAttestation(28);
        mark.setSecondAttestation(28);
        mark.setFinalExam(36);
        student.getTranscript().put(course, mark);

        String content = "I recommend " + student.getFirstName() + " " + student.getLastName() +
            ". GPA: " + String.format("%.2f", student.getGPA());
        RecommendationLetter letter = new RecommendationLetter(teacher, student, content, "Graduate School");

        assert letter.getAuthor().equals(teacher) : "FAIL: Author mismatch";
        assert letter.getStudent().equals(student) : "FAIL: Student mismatch";
        assert letter.getContent().contains("GPA") : "FAIL: Content should contain GPA";
        System.out.println("PASS: Letter creation works");
        System.out.println("PASS: Content contains student GPA");
    }
}
