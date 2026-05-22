package controllers;

import models.*;
import enums.*;
import core.DataStore;
import java.util.*;

public class ScheduleController {

    public Schedule generateSchedule(Course course, Teacher teacher, LessonType type) {
        DataStore ds = DataStore.getInstance();

        Room availableRoom = null;
        for (Room room : ds.getRooms()) {
            if (room.isAvailable() &&
                room.getCapacity() >= course.getEnrolledStudents().size()) {
                if (type == LessonType.LECTURE && room.getType() == RoomType.LECTURE_HALL) {
                    availableRoom = room;
                    break;
                } else if (type == LessonType.PRACTICE) {
                    availableRoom = room;
                    break;
                }
            }
        }

        if (availableRoom == null) {
            for (Room room : ds.getRooms()) {
                if (room.isAvailable()) { availableRoom = room; break; }
            }
        }

        if (availableRoom == null) return null;

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] slots = {"09:00-10:30", "11:00-12:30", "14:00-15:30", "16:00-17:30"};

        Set<String> usedSlots = new HashSet<>();
        for (Schedule s : ds.getSchedules()) {
            if (s.getRoom().getRoomNumber().equals(availableRoom.getRoomNumber())) {
                usedSlots.add(s.getDayOfWeek() + s.getTimeSlot());
            }
        }

        for (String day : days) {
            for (String slot : slots) {
                if (!usedSlots.contains(day + slot)) {
                    Schedule schedule = new Schedule(course, teacher, availableRoom, day, slot, type);
                    ds.addSchedule(schedule);
                    ds.log(teacher, "Schedule created: " + course.getName() + " " + day + " " + slot);
                    return schedule;
                }
            }
        }
        return null;
    }

    public List<Schedule> getScheduleForStudent(Student student) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule s : DataStore.getInstance().getSchedules()) {
            if (s.getCourse().getEnrolledStudents().contains(student)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Schedule> getScheduleForTeacher(Teacher teacher) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule s : DataStore.getInstance().getSchedules()) {
            if (s.getTeacher().equals(teacher)) {
                result.add(s);
            }
        }
        return result;
    }

    public String printWeeklySchedule(List<Schedule> schedules) {
        StringBuilder sb = new StringBuilder();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            boolean hasClass = false;
            for (Schedule s : schedules) {
                if (s.getDayOfWeek().equals(day)) {
                    if (!hasClass) {
                        sb.append("\n📅 ").append(day).append(":\n");
                        hasClass = true;
                    }
                    sb.append("  ").append(s.getTimeSlot())
                      .append(" | ").append(s.getCourse().getCourseCode())
                      .append(" | Room ").append(s.getRoom().getRoomNumber())
                      .append(" | ").append(s.getLessonType()).append("\n");
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : "No schedule available.";
    }
}
