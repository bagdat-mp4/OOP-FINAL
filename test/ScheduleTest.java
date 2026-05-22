package test;

import controllers.ScheduleController;
import models.Course;
import models.Teacher;
import models.Student;
import models.Schedule;
import models.Room;
import enums.CourseType;
import enums.LessonType;
import enums.RoomType;
import core.DataStore;

import java.util.List;

public class ScheduleTest {
    public static void test() {
        System.out.println("\n=== ScheduleTest ===");

        ScheduleController controller = new ScheduleController();
        DataStore ds = DataStore.getInstance();

        Room room = new Room("101", 50, RoomType.LECTURE_HALL);
        ds.addRoom(room);

        Course course = new Course(901L, "CS901", "Test Course", 3, CourseType.MAJOR, 1);
        Teacher teacher = new Teacher(902L, "Prof", "Test", "prof901@test.kz", "pass", 300000);

        Schedule schedule = controller.generateSchedule(course, teacher, LessonType.LECTURE);
        assert schedule != null : "FAIL: generateSchedule returned null";
        assert schedule.getRoom() != null : "FAIL: No room assigned";
        System.out.println("PASS: generateSchedule finds available room");

        Schedule schedule2 = controller.generateSchedule(course, teacher, LessonType.PRACTICE);
        assert schedule2 != null : "FAIL: Second schedule creation failed";
        String slot1 = schedule.getDayOfWeek() + schedule.getTimeSlot();
        String slot2 = schedule2.getDayOfWeek() + schedule2.getTimeSlot();
        assert !slot1.equals(slot2) : "FAIL: Time slot conflict detected";
        System.out.println("PASS: No time slot conflicts");
    }
}
