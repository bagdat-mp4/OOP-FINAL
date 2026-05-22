package core;

import models.*;
import enums.*;
import comparators.*;

import java.io.*;
import java.util.*;


public class DataStore implements Serializable {
    private static final String FILE_PATH = "data.ser";
    private static DataStore instance;

    private List<User> users = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Journal> journals = new ArrayList<>();
    private List<News> news = new ArrayList<>();
    private List<UserAction> logs = new ArrayList<>();
    private List<TechSupportRequest> techSupportRequests = new ArrayList<>();
    private List<ResearchProject> researchProjects = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private Map<User, ResearcherDecorator> researcherMap = new HashMap<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();
    private List<Attendance> attendances = new ArrayList<>();
    private List<RecommendationLetter> letters = new ArrayList<>();

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) {
            instance = loadFromFile();
            if (instance == null) {
                instance = new DataStore();
                instance.seedData();
            }
        }
        return instance;
    }

    private void seedData() {
        
        Admin admin = new Admin(1L, "Admin", "System", "admin@uni.kz", "admin123");
        users.add(admin);

        
        Teacher teacher = new Teacher(2L, "Aibek", "Seitkali", "aibek@uni.kz", "teacher123", 350000);
        teacher.setTitle(TeacherTitle.PROFESSOR);
        users.add(teacher);

        
        Student student = new Student(3L, "Ainur", "Bekova", "ainur@uni.kz", "student123", "CS", 2);
        users.add(student);

        
        GraduateStudent gradStudent = new GraduateStudent(4L, "Daniyar", "Seilov", "daniyar@uni.kz", "grad123", "CS", 1);
        users.add(gradStudent);

        
        Manager manager = new Manager(5L, "Gulnara", "Muratova", "gulnara@uni.kz", "manager123", 400000);
        manager.setManagerType(ManagerType.OR);
        users.add(manager);

        
        TechSupportSpecialist techSupport = new TechSupportSpecialist(6L, "Yerlan", "Asanov", "yerlan@uni.kz", "tech123", 250000);
        users.add(techSupport);

        
        Course course1 = new Course(1L, "CS101", "Introduction to Programming", 5, CourseType.MAJOR, 1);
        course1.addLectureInstructor(teacher);
        courses.add(course1);

        Course course2 = new Course(2L, "CS201", "Data Structures", 4, CourseType.MAJOR, 2);
        course2.addLectureInstructor(teacher);
        courses.add(course2);

        teacher.getActiveCourses().add(course1);
        teacher.getActiveCourses().add(course2);

        
        Journal journal = new Journal("KBTU Research Journal");
        journals.add(journal);

        
        News researchNews = new News("Top Researcher Award", "Research", "Prof. Aibek published a breakthrough paper!");
        news.add(researchNews);

        News generalNews = new News("University Holiday", "General", "University is closed on May 1st.");
        news.add(generalNews);

        
        ResearcherDecorator teacherResearcher = new ResearcherDecorator(teacher);
        ResearchPaper paper = new ResearchPaper("Deep Learning Methods", "KBTU Journal", "10.1000/xyz123", 15, new Date());
        paper.setCitations(10);
        paper.addAuthor(teacherResearcher);
        teacherResearcher.addPaper(paper);
        researcherMap.put(teacher, teacherResearcher);

        
        TechSupportRequest req = new TechSupportRequest(student, "Projector in room 301 is broken");
        techSupportRequests.add(req);

        
        Room r1 = new Room("101", 60, RoomType.LECTURE_HALL);
        Room r2 = new Room("202", 30, RoomType.SEMINAR_ROOM);
        Room r3 = new Room("303", 25, RoomType.COMPUTER_LAB);
        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private static DataStore loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (DataStore) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public void addLog(UserAction action) {
        logs.add(action);
    }

    public void log(User user, String details) {
        logs.add(new UserAction(user, details));
    }

    
    public List<User> getUsers() { return users; }
    public List<Course> getCourses() { return courses; }
    public List<Journal> getJournals() { return journals; }
    public List<News> getNews() { return news; }
    public List<UserAction> getLogs() { return logs; }
    public List<TechSupportRequest> getTechSupportRequests() { return techSupportRequests; }
    public List<ResearchProject> getResearchProjects() { return researchProjects; }
    public List<Message> getMessages() { return messages; }
    public Map<User, ResearcherDecorator> getResearcherMap() { return researcherMap; }
    public List<Room> getRooms() { return rooms; }
    public List<Schedule> getSchedules() { return schedules; }
    public List<Attendance> getAttendances() { return attendances; }
    public List<RecommendationLetter> getLetters() { return letters; }

    public void addUser(User u) { users.add(u); }
    public void removeUser(User u) { users.remove(u); }
    public void addCourse(Course c) { courses.add(c); }
    public void addNews(News n) { news.add(n); }
    public void addTechRequest(TechSupportRequest r) { techSupportRequests.add(r); }
    public void addResearchProject(ResearchProject p) { researchProjects.add(p); }
    public void addMessage(Message m) { messages.add(m); }
    public void addRoom(Room r) { rooms.add(r); }
    public void addSchedule(Schedule s) { schedules.add(s); }
    public void addAttendance(Attendance a) { attendances.add(a); }
    public void addLetter(RecommendationLetter l) { letters.add(l); }

    public void makeResearcher(User u) {
        if (!researcherMap.containsKey(u)) {
            researcherMap.put(u, new ResearcherDecorator(u));
        }
    }

    public ResearcherDecorator getResearcher(User u) {
        return researcherMap.get(u);
    }

    public boolean isResearcher(User u) {
        return researcherMap.containsKey(u);
    }

    public User findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    public void printAllUniversityPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> all = new ArrayList<>();
        for (ResearcherDecorator rd : researcherMap.values()) {
            all.addAll(rd.getPapers());
        }
        all.sort(c);
        all.forEach(p -> System.out.println(p));
    }

    public void printTopCitedResearcher() {
        researcherMap.entrySet().stream()
            .max(Comparator.comparingDouble(e -> e.getValue().calculateHIndex()))
            .ifPresent(e -> System.out.println("Top Researcher: " + e.getKey().getFirstName() +
                " | H-index: " + e.getValue().calculateHIndex()));
    }
}
