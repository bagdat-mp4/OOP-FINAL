package models;

import enums.LessonType;
import java.io.Serializable;

public class Schedule implements Serializable {
    private Course course;
    private Teacher teacher;
    private Room room;
    private String dayOfWeek;
    private String timeSlot;
    private LessonType lessonType;

    public Schedule(Course course, Teacher teacher, Room room,
                    String dayOfWeek, String timeSlot, LessonType lessonType) {
        this.course = course;
        this.teacher = teacher;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.lessonType = lessonType;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    @Override
    public String toString() {
        return dayOfWeek + " " + timeSlot + " | " + course.getCourseCode() +
               " | Room: " + room.getRoomNumber() + " | " + lessonType +
               " | " + teacher.getFirstName();
    }
}
