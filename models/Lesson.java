package models;

import enums.LessonType;

import java.io.*;
import java.util.*;

/**
 * Lesson class representing scheduled classes
 */
public class Lesson implements Serializable {

    private LessonType lessonType;
    private String room;
    private Date schedule;
    private Course course;

    /**
     * Default constructor
     */
    public Lesson() {
    }

    /**
     * Constructor with parameters
     */
    public Lesson(LessonType lessonType, String room, Date schedule, Course course) {
        this.lessonType = lessonType;
        this.room = room;
        this.schedule = schedule;
        this.course = course;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return lessonType + " in room " + room;
    }

}